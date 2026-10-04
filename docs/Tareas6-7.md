# Tareas 6 y 7: Profile y CRUD de reseñas

Rama Android: `task/#6-7`, basada en `origin/develop` (`0a05ba3`). Backend revisado en `develop` (`9621847`), sin cambios de código.

## Arquitectura

Las vistas observan StateFlow en sus ViewModels Hilt. Los ViewModels acceden a repositories, interfaces RemoteDataSource y sus implementaciones Retrofit. RemoteDataModule enlaza las interfaces a las implementaciones; AppModule conserva la configuración de Retrofit del equipo.

Los modelos de presentación mantienen los recursos locales para previews y pantallas aún no migradas. Los campos opcionales y RemoteContentMappers permiten mostrar nombres, biografía, imágenes, producto, título y texto de la API sin usar IDs de recursos como IDs de base de datos. Profile conserva ProfileHeader y ProfileProductCard; CreateReview, RateProduct y Review conservan sus componentes originales.

## Recorridos

- Buscar usuarios → resultados del backend → Profile: `GET /users/{id}` y `GET /users/{id}/reviews`. Solo se muestran reseñas del usuario seleccionado. Hay carga, error, reintento y estado vacío; regresar desde otra pantalla vuelve a consultar.
- Crear reseña → seleccionar producto real (`GET /articles`) → formulario RateProduct → publicar (`POST /reviews`). El usuario temporal se define una sola vez en `CURRENT_USER_ID = 1`. La navegación ocurre después del éxito; los errores conservan el borrador y no permiten envíos simultáneos.
- Crear reseña → Mis reseñas: consulta `GET /users/1/reviews`; permite editar con `PUT /reviews/{id}` y eliminar con confirmación usando `DELETE /reviews/{id}`. La lista se actualiza tras el éxito. Solo se ofrecen mutaciones para el usuario temporal y se vuelve a comprobar el propietario antes de enviar. Esto no sustituye autenticación/autorización del servidor.
- Profile → Review: detalle remoto con autor y producto reales; el autor lleva a Profile y las reseñas propias ofrecen acceso a administración.

Ventajas y desventajas se agregan al cuerpo porque el contrato del backend solo dispone de `title`, `body` y `rating`. Al editar se conserva todo ese cuerpo.

## Integración con el equipo

OwnProfile, Home y Product permanecen en manos de sus tareas respectivas. La tarea 4 debe entregar el `article.id` real a `RateProduct.createRoute`; el argumento interno de esa ruta ahora es `productId`. El Product local todavía envía un recurso Android; si se entra por ese flujo, el formulario muestra error y permite volver al catálogo remoto. El resumen del producto en una reseña remota no abre el detalle local para evitar mostrar un artículo equivocado. Reactivar `onProductClick` con el ID real cuando Product consuma la API.

Los accesos desde Home/Activity/OwnProfile aún locales necesitan que sus respectivas tareas migren los identificadores; no se inventan equivalencias entre datos locales y del servidor.

## Validación y entorno local

- Android: `./gradlew :app:assembleDebug :app:testDebugUnitTest`.
- Pruebas de Profile, cambio rápido de usuario, errores, borradores, usuario temporal, permisos de edición/eliminación, detalle remoto y contrato HTTP (incluido DELETE 204).
- Backend: `npm test` pasa las 7 pruebas existentes. Estas usan el almacén en memoria del repositorio.
- La comprobación adicional con PostgreSQL local detectó que `users.firebase_uid` no existe en la base `devicers`: el esquema local debe actualizarse según los modelos del equipo antes de probar la app contra esa base. No se modificaron ni eliminaron sus datos.
- El puerto 3000 está ocupado por otra aplicación. Retrofit mantiene `http://10.0.2.2:3000/`, conforme a AppModule. Resolver el puerto antes de ejecutar Devicers localmente.
- No había dispositivo/emulador conectado durante la validación; no se afirma una prueba visual en Android.
