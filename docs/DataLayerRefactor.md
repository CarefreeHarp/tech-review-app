# Capa de datos: Firestore y Express

La UI utiliza los repositorios agrupados, los modelos comunes y la implementación seleccionada para los datos generales. Firebase Auth mantiene la sesión y Firebase Storage conserva las cargas de archivos en ambos modos. Se retiraron los consumidores de repositorios específicos de pantalla y las referencias a LocalProviders. No se mantienen adaptadores de compatibilidad que oculten esos contratos anteriores.

## Selección de implementación

El selector está en `DevicersApp/app/src/main/java/com/example/devicersapp/data/config/BackendConfig.kt`:

```kotlin
object BackendConfig {
    const val USE_FIRESTORE = true
}
```

`true` selecciona Firestore y `false` selecciona Express mediante Retrofit. Se cambia directamente en Kotlin y se recompila la aplicación; no requiere una propiedad de Gradle. En la vista Android Studio, está en `app → kotlin+java → com.example.devicersapp → data → config → BackendConfig`.

`DataSourceModule` recibe directamente ambas implementaciones y aplica `BackendConfig.USE_FIRESTORE` para elegir cuál devuelve en todos los contratos. Hilt crea los objetos necesarios para entregar esos parámetros; esa construcción no ejecuta consultas a las bases de datos. No existe cambio automático de base ante un error, ni cambio de implementación en caliente.

`ServiceModule` construye Retrofit con `BuildConfig.DEVICERS_API_BASE_URL`. En el emulador su valor predeterminado es `http://10.0.2.2:3000/`; puede configurarse con `-PdevicersApiBaseUrl=...`.

Firebase Auth y Firebase Storage son servicios compartidos por ambos modos. El selector cambia la persistencia de las entidades entre Firestore y Express/SQL. No sincroniza las bases después de una escritura: cada modo modifica su propia base.

## Responsabilidades y dependencias

- `data/datasource/`: contratos por responsabilidad que devuelven modelos comunes, nunca DTO propios del transporte.
- `data/datasource/implementations/firestore/`: rutas documentales, transacciones, decodificación y traducción al modelo común.
- `data/datasource/implementations/retrofit/`: endpoints, estrategias de consulta HTTP y traducción al mismo modelo común.
- `data/datasource/services/`: interfaces HTTP que reciben y devuelven DTO.
- `data/dto/ResponseDTO/firestore/`, `retrofit/` y `shared/`: formatos de lectura. Categorías, tiendas y vínculos de tiendas comparten DTO porque sus campos coinciden. El ID del artículo de un vínculo se conoce por la operación solicitada.
- `data/dto/RequestDTO/retrofit/` y `RequestDTO/firestore/`: cuerpos HTTP y campos documentales de las escrituras existentes. Crear una reseña por REST usa `userId`/`articleId`; actualizarla usa `user_id`/`article_id`.
- `data/repository/`: operaciones de usuarios, artículos, reseñas, comentarios, likes, seguimientos, guardados, categorías, tiendas, notificaciones y solicitudes de productos. Autenticación y archivos conservan responsabilidades propias. No existe un repositorio de sesión.
- `ui/models/`: ubicación conservada para los modelos de la app. Estos representan entidades y agregaciones; no son el `State` de una pantalla.

Los DTO convierten sus campos a los modelos comunes en su mismo archivo. Un DTO de usuario también puede producir `UserSummary`; se eliminó el DTO de resumen que rellenaba artificialmente un usuario completo. Los modelos públicos, agregados y completos pueden coexistir cuando contienen información distinta.

Las lecturas del SDK cliente de Firestore se realizan directamente en cada implementación, sin un archivo auxiliar `FirestoreReads.kt`.

Las lecturas básicas de usuarios devuelven el perfil sin reseñas. Las listas básicas de artículos devuelven la ficha y `storesCount`; las tiendas se obtienen explícitamente y se incluyen al consultar el detalle. Retrofit normaliza estas proyecciones aunque Express envíe asociaciones adicionales. Los DTO conservan las asociaciones HTTP disponibles; `UserRepository.getProfile`, `ReviewRepository.getReviewSummariesByUser` y `getReviewDetail` reúnen las relaciones necesarias. El detalle de producto combina artículos, reseñas activas y autores; las búsquedas combinan artículos, categorías y calificaciones sin depender de la tecnología seleccionada. `ReviewRepository.getFeedReviews` usa los contadores y calcula los promedios desde las reseñas activas.

Los límites no son una prohibición de leer entidades relacionadas: por ejemplo, la implementación Retrofit de likes de comentarios consulta comentarios para recuperar el contexto de reseña que Firestore obtiene de la ruta. `CommentRepository.getThread` conserva el nombre y la foto almacenados en cada comentario, ordena el hilo y evita repetir nodos con referencias cíclicas. No consulta likes de comentarios. Cuando un comentario no contiene foto del autor, `CommentRepository` resuelve los IDs únicos necesarios mediante `UserRemoteDataSource.getByIds`. `CommentThreadItem.authorImageUrl` lleva esa foto a la presentación sin cambiar el documento ni la copia original en `CommentInfo`. Las fotos incluidas en comentarios se conservan.

Firebase Auth mantiene y restaura la sesión. `data/utils/Utils.kt` consulta directamente `FirebaseAuth.getInstance().currentUser` y su UID; no guarda una copia. `AuthRepository` solo ejecuta inicio de sesión, registro, cierre de sesión y actualización de la cuenta Auth, conservando los mensajes por tipo de error. No tiene StateFlows de usuario ni un perfil global.

El registro crea primero la cuenta Auth y después llama a `UserRepository.createIfMissing` usando el UID actual. `SplashViewModel` comprueba únicamente si Auth tiene usuario, sin depender de Firestore ni de Express para decidir el destino inicial. `UserRepository.getAuthenticatedUser` consulta el perfil por ese UID cuando una pantalla necesita su información general o su ID numérico existente. La edición usa ese perfil y Firebase Storage; volver a OwnProfile refresca su estado de pantalla.

Se eliminaron `SessionState`, `SessionViewModel`, `SessionRepository`, `LocalSessionState` y su entry point de debug. Los componentes reciben nombres y fotos consultados por su pantalla. Al abrir un perfil, `ProfileViewModel` resuelve el UID de Auth mediante `UserRepository.getAuthenticatedUser` y compara el ID numérico resultante con el ID de navegación. Si coinciden, solicita navegar a OwnProfile antes de consultar y renderizar el perfil público. No mantiene un ViewModel global para esa identidad. El avatar de Activity recibe también el ID de aplicación de su propio estado.

## Inyección

| Archivo | Responsabilidad |
| --- | --- |
| `DataSourceModule` | Seleccionar una implementación para cada contrato; instancias singleton para los contratos y recepción directa de ambas implementaciones. |
| `RepositoryModule` | Construir los repositorios agrupados sin duplicar constructores `@Inject`. |
| `ServiceModule` | Construir Retrofit y sus servicios HTTP. |
| `FirebaseHiltModule` | Proporcionar exclusivamente Firebase Auth, Firestore y Storage. |

`AppModule` y `DiscoveryModule` se retiraron. Las clases de autenticación y archivos que conservan constructor `@Inject` siguen siendo construidas automáticamente por Hilt.

## Contraste entidad por entidad

Las fechas de Firestore se convierten con segundos y nanosegundos; no se pasan por `Date`, que truncaría precisión. Las respuestas SQL priorizan `created_at`/`updated_at` sobre los alias históricos `createdAt`/`updatedAt`. Los errores y cancelaciones conservan su causa; no se sustituyen datos fallidos por muestras locales.

| Entidad y contrato | Firestore | Express | Modelo común y particularidades |
| --- | --- | --- | --- |
| Usuario / `UserRemoteDataSource` | `users/{id}` | `GET /users`, `/users/:id`; consulta por UID de registro, POST y PUT | `UserInfo`, `UserSummary`. id, firebase_uid, email, username, biography, profile_image_url, notifications_last_viewed_at, is_active y fechas. Firestore añade followers_count, follows_count y review_bookmarks_count. |
| Artículo / `ProductRemoteDataSource` | `articles/{id}` | `GET /articles`, `/articles/:id` | `ProductInfo`. category_id/category_name, brand_name, name, model, description, image_url, release_date, specifications, stores_count, is_active y fechas. `brandId` es opcional y solo existe en SQL; no se inventa una marca documental ni un ID. |
| Tiendas del artículo / mismo contrato de artículos | `articles/{articleId}/stores/{storeId}` | `stores` de la respuesta de artículo | `ArticleStoreInfo`: store_id, name, website_url, product_url. Conserva las copias del vínculo aunque difieran del catálogo de tiendas. |
| Categoría / `CategoryRemoteDataSource` | `categories/{id}` | `GET /categories` | `CategoryInfo`: id, parent_category_id, name, description. |
| Tienda / `StoreRemoteDataSource` | `stores/{id}` | `GET /stores` | `StoreInfo`: id, name, website_url. |
| Reseña / `ReviewRemoteDataSource` | `reviews/{id}` | `GET /reviews`, `/reviews/:id`, `/users/:id/reviews`, `/articles/:id/reviews`; POST y PUT | `ReviewInfo`: id, user_id, article_id, rating, title, body, likes_count, comments_count, is_active y fechas. SQL calcula los contadores; Firestore los guarda. Crear/actualizar por REST vuelve a consultar el detalle para obtener los contadores que no devuelve la respuesta antigua de escritura. |
| Comentario / `CommentRemoteDataSource` | `reviews/{reviewId}/comments/{commentId}` | `GET /comments` | `CommentInfo`: id, user_id, user_username, user_profile_image_url, parent_comment_id, body, is_active y fechas; Firestore añade likes_count. reviewId se obtiene de la ruta o de review_id en SQL. |
| Like de reseña / `ReviewLikeRemoteDataSource` | `reviews/{reviewId}/likes/{userId}` | `GET /review-likes` | `ReviewLikeInfo`: userId, reviewId y createdAt. El ID independiente es opcional y solo existe en SQL. |
| Like de comentario / `CommentLikeRemoteDataSource` | `reviews/{reviewId}/comments/{commentId}/likes/{userId}` | `GET /comment-likes` y `/comments` para resolver reviewId | `CommentLikeInfo`: userId, commentId, reviewId y createdAt; ID independiente opcional. |
| Seguimiento / `FollowRemoteDataSource` | `users/{userId}/follows/{followedId}` y `followers/{followerId}` | `GET /follows`, filtrado por sentido | `FollowInfo`: followerId, followedId, createdAt. Ambas subcolecciones representan una misma relación. |
| Guardado / `ReviewBookmarkRemoteDataSource` | `users/{userId}/review_bookmarks/{reviewId}` | `GET /review-bookmarks`, filtrado por usuario | `ReviewBookmarkInfo`: userId, reviewId, createdAt. |
| Notificación / `NotificationRemoteDataSource` | `notifications/{id}` | `GET /notifications`, filtrado por destinatario | `NotificationInfo`: id, user_id, actor_user_id, type, review_id, comment_id, is_read y created_at. Los tipos originales se conservan. |
| Solicitud de producto / `ProductRequestRemoteDataSource` | `product_requests/{requestId}` | `GET/POST /product-requests` | `ProductRequestInfo`: ID textual, user_id, product_name, image_url, category, brand, release_date, status y createdAt. No contiene firebase_uid. |
| Secuencias técnicas | `counters/next_id` | Secuencias SQL | next_user_id y next_review_id se actualizan junto con la creación, usando merge para conservar el otro contador. No es una entidad de UI. |

`specifications` conserva exactamente el string JSON, sin serializarlo por segunda vez. Los objetos opcionales, los valores nulos y las copias de comentarios/tiendas conservan sus valores. El esquema nuevo no tiene una colección de marcas: Android usa `brandName` del artículo; los identificadores relacionales permanecen disponibles cuando proceden de SQL.

La eliminación de una reseña desde Android es lógica en ambos modos: `is_active=false`. Las subcolecciones, comentarios, likes y demás referencias se conservan. No se utiliza el DELETE físico existente de Express.

Los perfiles se editan mediante PUT y el ID de aplicación. El UID permanece como atributo de registro para encontrar ese ID después del inicio de sesión; un reintento de registro conserva el perfil existente. Express no incorpora Firebase Admin, cuentas de servicio ni verificación de tokens. El SDK Admin facilitado al agente se reserva a sus tareas administrativas externas. Esta etapa no añade escrituras de likes, follows ni generación de notificaciones.

## UI y funcionalidades pospuestas por solicitud del usuario

Las pantallas de producto, creación y edición de reseñas, solicitudes de productos, perfiles, búsqueda de perfiles, guardados y Home consumen los contratos agrupados. Sus ViewModels producen los modelos de presentación y los estados completos que reciben los composables. Los errores de carga y escritura usan recursos de texto. Las previews ya no importan la carpeta `data.local` ni construyen una sesión propia.

Likes y seguimientos mantienen exclusivamente su diseño:

- FollowList conserva el ID del perfil y la pestaña seleccionada, sin consultar seguidores, seguidos ni perfiles relacionados.
- Home conserva Para ti y Siguiendo. Para ti carga el feed general; Siguiendo muestra una lista vacía y no consulta relaciones.
- Profile, OwnProfile, guardados y búsqueda de perfiles no consultan seguimientos. Los controles y sus conteos visibles permanecen como presentación sin acciones remotas.
- Las filas de reseñas y comentarios no muestran conteos reales de likes ni consultan sus subcolecciones. No crean ni eliminan likes.
- Activity carga solamente el perfil necesario para su cabecera; no consulta, envía, guarda ni elimina notificaciones. Su lista permanece vacía.

Los contratos de datos por entidad se conservan disponibles, pero la UI y los repositorios agregados utilizados por ella no ejecutan consultas de likes o seguimientos. Los datos existentes de esas relaciones no se borraron de ninguna base.

## Contadores de subcolecciones en Firestore

Cada contador describe el número de documentos directos de su subcolección, incluidos documentos inactivos si aún existen físicamente:

| Documento padre | Campos |
| --- | --- |
| `users/{userId}` | `followers_count`, `follows_count`, `review_bookmarks_count` |
| `articles/{articleId}` | `stores_count` |
| `reviews/{reviewId}` | `likes_count`, `comments_count` |
| `reviews/{reviewId}/comments/{commentId}` | `likes_count` |

Los contadores se calcularon a partir de los documentos existentes y se escribieron en una transacción administrativa. Se verificaron los 123 documentos de la base: solo se añadieron o recalcularon estos campos en 50 documentos padre; las demás propiedades permanecieron iguales. La creación de usuarios inicializa sus tres contadores en cero. Crear reseñas ya inicializa sus contadores en cero. La eliminación lógica no reduce la cantidad de documentos físicos.

Los DTO de Firestore y los modelos comunes conservan los nuevos atributos. No se añadieron consultas a subcolecciones para presentarlos ni un sistema de seguimiento o likes. El diagrama Mermaid y el SVG incluidos en el README muestran estos campos.

## Verificación

Se contrastaron los contratos y mapeos con Firestore y con las respuestas reales de Express en ejecución, sin resetear la base. Se generó el APK completo mediante `:app:assembleDebug` con ambas implementaciones seleccionadas. La compilación limpia también verificó el grafo completo de Hilt.

La comprobación funcional se realiza directamente en el emulador y contra la persistencia real, con cuentas y registros temporales separados de los datos existentes. Por solicitud del usuario se retiraron los archivos, dependencias, scripts de Postman y accesos de configuración dedicados a tests; no se añadieron archivos de test. La sesión original se respalda antes de cambiar de cuenta. Likes, seguimientos y envío de comentarios siguen desconectados.


El registro exige un nombre no vacío antes de crear la cuenta Auth, evitando cuentas sin perfil por ese error de validación. La solicitud de producto usa el campo compartido `ui/utils/search/LaunchDateField`: mantiene el cursor al aplicar separadores, de modo que escribir `01012024` conserva `01 / 01 / 2024` al guardar.

Las cargas de fotos utilizan Firebase Storage en ambos modos. El usuario autorizó cambiar exclusivamente la fecha de vencimiento de su regla existente: de `timestamp.date(2026, 10, 15)` a `timestamp.date(2026, 12, 31)`. Se comprobó que el resto del contenido y los permisos permanecieran idénticos. No se integró Firebase Admin en Android ni en Express; la publicación se realizó únicamente como tarea administrativa.

Se comprobaron las escrituras desde la app con ambas implementaciones: creación del perfil al registrarse, edición de nombre y biografía, creación y edición de reseñas, retirada lógica de reseñas y envío de solicitudes de productos. Después de cada recorrido se contrastaron los valores persistidos. También se verificaron acceso válido, error personalizado de credenciales, cierre y persistencia de Auth, lecturas de guardados, catálogo, perfiles, comentarios y navegación. Tras cambiar únicamente la fecha autorizada de Storage, la subida de fotos funcionó en ambos modos: se verificaron el archivo subido, la URL persistida en Firestore o SQL, la foto de Firebase Auth y una descarga HTTP 200.

Las cuentas, reseñas, solicitudes, guardados e imágenes temporales de la comprobación se retiraron al terminar. Se contrastaron de nuevo los registros originales de Firestore y SQL con su respaldo y se restauró la sesión original del emulador. La configuración final conserva `BackendConfig.USE_FIRESTORE = true`.
