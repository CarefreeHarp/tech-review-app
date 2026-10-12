# Firestore: tareas 12, 13, 14 y 15

## Alcance

- FoundProducts lee artículos activos, categorías y reseñas activas. Mantiene búsqueda por nombre/modelo/descripción, jerarquía de categorías, calificación mínima y orden reciente/calificación. Una búsqueda nueva cancela la carga anterior.
- ProfileSearchResults consulta usuarios activos, excluye el perfil de sesión, cuenta reseñas activas y lee seguimientos. Las fotos vienen de `users.profile_image_url`.
- Activity lee notificaciones del ID numérico de sesión y resuelve actores y destinos vigentes. No usa perfiles/reseñas de muestra ni escribe seguimientos o estados de lectura. Los botones de seguimiento son informativos y están deshabilitados.
- RequestProduct guarda una sugerencia sin fotos. Valida nombre y fecha opcional, bloquea envíos simultáneos y comunica éxito únicamente después de confirmar la transacción.

MVVM: View → ViewModel → Repository → DataSource. `DiscoveryModule` agrega bindings exclusivos y reutiliza las instancias de Firebase existentes. No cambia `AppModule`, `FirebaseHiltModule`, navegación, DTOs ni repositorios de compañeros.

## Contratos comprobados

Se conserva el contrato de `tasks/#5-6-7`: documentos con IDs numéricos, campos en snake_case y fechas `Timestamp` o texto ISO. Colecciones de lectura: `articles`, `categories`, `reviews`, `users`, `follows`, `notifications`.

`notifications` se consulta por `user_id`; usa `actor_user_id`, `type`, `review_id` y `created_at`. Tipos admitidos: `review_like`, `comment_like`, `comment`, `follow`. Se omiten eventos sin actor activo o sin destino vigente y tipos desconocidos. La agrupación usa días de calendario en la zona del dispositivo.

Las consultas con `Source.SERVER` exponen errores de conexión en lugar de presentar datos locales como una lectura remota exitosa. No se requieren índices compuestos: se filtra por un campo en Firestore y se ordena localmente.

## Nueva colección: product_requests

El documento usa un UUID por intento. Reintentar sin editar conserva ese ID y no duplica ni cambia la fecha de una solicitud ya confirmada. Editar inicia una sugerencia nueva.

| Campo | Tipo | Contenido |
|---|---|---|
| user_id | entero | ID numérico del perfil de sesión |
| firebase_uid | texto | UID autenticado, contrastado con el perfil |
| product_name | texto | Nombre obligatorio, sin espacios en extremos |
| category | texto | Categoría opcional |
| brand | texto | Marca opcional |
| release_date | texto | Fecha opcional en DD / MM / AAAA; vacío si no se especifica |
| status | texto | `pending` |
| createdAt | Timestamp | Fecha del servidor |

No se escribe en artículos ni se cargan imágenes. La transacción verifica el UID y estado activo del perfil. La transacción necesita permisos de **get y create**, no solo create:

- `get users/{user_id}` para comprobar que el perfil existe, está activo y su `firebase_uid` coincide con Auth.
- `get product_requests/{requestId}` **también cuando el documento todavía no existe**. Una condición que consulta exclusivamente `resource.data.firebase_uid` no autoriza ese primer get. La política debe resolver explícitamente el caso ausente sin permitir leer documentos existentes de otros usuarios ni listar toda la colección.
- `create product_requests/{requestId}` ligado al UID autenticado y al ID numérico del perfil activo. Debe validar el conjunto exacto de campos, sus tipos, nombre obligatorio, fecha opcional, `status == pending` y fecha del servidor. Rechazar UID/ID ajenos y campos adicionales.
- La aplicación no requiere `list`, `update` ni `delete` de solicitudes. No ampliar esos permisos para facilitar pruebas o limpieza.

Las comprobaciones del cliente no sustituyen controles de servidor. Este cambio no modifica ni despliega reglas. **No hay reglas versionadas ni se han obtenido las reglas desplegadas; su conformidad permanece pendiente.** La prueba histórica exitosa con una cuenta no demuestra aislamiento entre usuarios.

Casos de reglas por ejecutar sobre el contenido desplegado: get ausente autenticado, creación válida, reintento con get propio, get/list de solicitudes ajenas, UID/ID falsos, perfil inactivo, campos indebidos, update/delete ajenos y acceso sin autenticación. Las lecturas de las otras seis colecciones también deben probarse con las consultas reales, porque las reglas no filtran resultados después de leerlos.

Referencia oficial: [estructura y operaciones de reglas](https://firebase.google.com/docs/firestore/security/rules-structure) y [condiciones y consultas](https://firebase.google.com/docs/firestore/security/rules-conditions).

## Verificación

Las pruebas unitarias cubren filtros, categorías descendientes, reseñas inactivas, búsquedas concurrentes, errores/reintentos, perfiles de sesión, destinos de actividad, validación y envío único de solicitudes. Solo se adaptan los casos de FoundProducts y ProfileSearchResults en las pruebas compartidas autorizadas.

`DiscoveryUiTest` es una prueba de componente: muestra actores remotos y comprueba el ID emitido por el clic y el botón de reintento. **No certifica navegación.**

`ActivityRemoteNavigationTest` usa MainActivity y AppNavigation de producción, pulsa una notificación y comprueba el cuerpo de su reseña real. Exige un ID que no exista en LocalReviewProvider. Se ejecuta en la combinación con `tasks/#5-6-7`, con `devicersTestStartDestination=activity` y los argumentos `discoveryRemoteReview=true`, `discoveryTestEmail` y `discoveryTestPassword`. Solo lee datos; no genera notificaciones ni modifica reseñas/perfiles. Su EntryPoint de sesión vive en src/debug y no se incluye en release.

`DiscoveryFirestoreFlowTest` es opt-in: recibe `discoveryTestEmail` y `discoveryTestPassword` como argumentos de instrumentación. Exige además `discoveryDisposableEnvironment=true` y debe ejecutarse exclusivamente en un entorno de prueba desechable con permisos de limpieza propios, no contra producción. Requiere una cuenta de prueba previamente provisionada y datos de prueba de catálogo/actividad. Usa una instancia Firebase separada; crea únicamente una solicitud con prefijo `discovery-test-`, verifica el reintento y elimina ese documento al terminar. No modifica perfiles, reseñas, seguimientos ni notificaciones.

La rama base conserva las pantallas Product/Profile/Review de develop. Su migración pertenece a las otras tareas: los resultados nuevos navegan con los mismos IDs, y no sustituyen las implementaciones de esos destinos.


## Dependencias y límites de validación

Activity ahora usa `Review.createRoute` para IDs de notificaciones remotas. Conserva las rutas locales de otros flujos. La lectura Firestore del destino Review procede de la tarea 5–7; Profile sigue dependiendo de la tarea 4. Una combinación sin conflictos no demuestra por sí sola navegación funcional.

La protección de solicitudes cubre doble clic y reintento del mismo intento mientras vive el ViewModel. No es deduplicación global después de muerte del proceso, nueva visita o edición.

La validación anterior de 48 unitarias cubre lógica con fuentes falsas y contratos anteriores; no equivale a 48 pruebas de integración ni a pruebas de reglas desplegadas. Cada nueva ejecución debe registrar por separado compilación, unitarias, componentes, navegación integrada y reglas.

### Ejecución tras la auditoría (11 de octubre de 2026)

- Rama de tareas: assembleDebug, testDebugUnitTest y assembleDebugAndroidTest correctos; 48 unitarias sin fallos.
- Combinación temporal con tasks/#5-6-7 en 31f6a8d: mismas compilaciones correctas y 48 unitarias sin fallos; sin merge a develop.
- Dos pruebas de componente aprobadas. Prueba de navegación integrada aprobada: abre una reseña de Firestore con ID ausente de LocalReviewProvider y verifica su cuerpo visible después de la carga y el desplazamiento. Solo lecturas; no se ejecutó la prueba de creación/limpieza contra producción.
- Firebase Console, con la cuenta abierta, indicó proyecto inexistente o falta de permisos. No se pudieron obtener ni verificar las reglas desplegadas. No se confirma un fallo de permisos.
