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

No se escribe en artículos ni se cargan imágenes. La transacción verifica el UID y estado activo del perfil. Las reglas de Firestore desplegadas deben permitir la creación al propietario; la comprobación en cliente no sustituye las reglas de acceso. Este cambio no modifica ni despliega reglas.

## Verificación

Las pruebas unitarias cubren filtros, categorías descendientes, reseñas inactivas, búsquedas concurrentes, errores/reintentos, perfiles de sesión, destinos de actividad, validación y envío único de solicitudes. Solo se adaptan los casos de FoundProducts y ProfileSearchResults en las pruebas compartidas autorizadas.

`DiscoveryUiTest` comprueba actores y reseñas que no existen en los fixtures locales y el botón de reintento.

`DiscoveryFirestoreFlowTest` es opt-in: recibe `discoveryTestEmail` y `discoveryTestPassword` como argumentos de instrumentación. Requiere una cuenta de prueba previamente provisionada y datos de prueba de catálogo/actividad. Usa una instancia Firebase separada; crea únicamente una solicitud con prefijo `discovery-test-`, verifica el reintento y elimina ese documento al terminar. No modifica perfiles, reseñas, seguimientos ni notificaciones.

La rama base conserva las pantallas Product/Profile/Review de develop. Su migración pertenece a las otras tareas: los resultados nuevos navegan con los mismos IDs, y no sustituyen las implementaciones de esos destinos.
