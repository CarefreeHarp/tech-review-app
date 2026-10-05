# Tareas 6 y 7: Profile y CRUD de reseñas

Rama Android: `task/#6-7`, con `origin/task/#4` (`972c9a4`) integrado sobre la base `origin/develop` (`0a05ba3`). Backend revisado en `develop` (`9621847`), sin cambios de código.

## Arquitectura

Las vistas observan StateFlow en sus ViewModels Hilt. Los ViewModels acceden a repositories, interfaces RemoteDataSource y sus implementaciones Retrofit. AppModule conserva los servicios Retrofit y los proveedores de Product/Review de la tarea 4; RemoteDataModule enlaza únicamente Users. ProductRepository y ReviewRepository conservan el contrato Result de la tarea 4, propagan cancelaciones y son compartidos por todos los flujos.

Los modelos de presentación mantienen los recursos locales para previews y pantallas aún no migradas. Los campos opcionales y RemoteContentMappers permiten mostrar nombres, biografía, imágenes, producto, título y texto de la API sin usar IDs de recursos como IDs de base de datos. Profile conserva ProfileHeader y ProfileProductCard; CreateReview, RateProduct y Review conservan sus componentes originales.

## Recorridos

- Buscar usuarios → resultados del backend → Profile: `GET /users/{id}` y `GET /users/{id}/reviews`. Solo se muestran reseñas del usuario seleccionado. Hay carga, error, reintento y estado vacío; regresar desde otra pantalla vuelve a consultar.
- Crear reseña → seleccionar producto real (`GET /articles`) → formulario RateProduct → publicar (`POST /reviews`). El usuario temporal se define una sola vez en `CURRENT_USER_ID = 1`. La navegación ocurre después del éxito; los errores conservan el borrador y no permiten envíos simultáneos.
- Crear reseña → Mis reseñas: consulta `GET /users/1/reviews`; permite editar con `PUT /reviews/{id}` y eliminar con confirmación usando `DELETE /reviews/{id}`. La lista se actualiza tras el éxito. Solo se ofrecen mutaciones para el usuario temporal y se vuelve a comprobar el propietario antes de enviar. Esto no sustituye autenticación/autorización del servidor.
- Profile → Review: detalle remoto con autor y producto reales; el autor lleva a Profile y las reseñas propias ofrecen acceso a administración.

Ventajas y desventajas se agregan al cuerpo porque el contrato del backend solo dispone de `title`, `body` y `rating`. Al editar se conserva todo ese cuerpo.

## Integración con el equipo

La tarea 4 conserva su catálogo, filtros, Product y fuentes Retrofit. CreateReview usa su catálogo y añade el acceso a Mis reseñas. RateProduct incorpora la publicación protegida contra envíos simultáneos y conserva el borrador ante errores. Review añade autor real, navegación a Profile y acceso a administración. No hay implementaciones paralelas de Product/Review ni registros Hilt duplicados.

Product → RateProduct, Profile → Review y Review → Product usan los IDs reales del backend. Al publicar se retira el formulario del historial tanto si se entró desde Product como desde CreateReview. Product vuelve a consultar sus reseñas al regresar. Se mantiene una sola dependencia foundation-layout, administrada por el BOM existente.
Home, Activity, OwnProfile y reseñas guardadas aún muestran datos locales. Sus accesos a detalle usan una ruta local separada, por lo que «Ver más» muestra la misma reseña sin enviar su ID de ejemplo al backend. La navegación entre estas pantallas y los datos remotos todavía requiere que sus respectivas tareas migren los identificadores; no se inventan equivalencias entre IDs locales y del servidor.

## Validación y entorno local

- Android: `./gradlew :app:assembleDebug :app:testDebugUnitTest`.
- Pruebas de Profile, cambio rápido de usuario, errores, borradores, usuario temporal, permisos de edición/eliminación, detalle remoto y contrato HTTP (incluido DELETE 204).
- Backend: `npm test` pasa las 7 pruebas existentes. Estas usan el almacén en memoria del repositorio.
- Una base PostgreSQL temporal con el esquema y los datos iniciales actuales pasó consultas de Profile y el ciclo POST, GET, PUT y DELETE. Se eliminó al finalizar.
- Una prueba instrumentada en Android 16 pasó desde un emulador contra esa API temporal: Profile, reseñas del usuario seleccionado y CRUD con `userId = 1`. Tras integrar la tarea 4 pasaron las 4 pruebas instrumentadas y las 13 pruebas unitarias Android.
- `HomeLocalReviewNavigationTest` comprueba en el emulador que «Ver más» abre el detalle de la reseña local seleccionada aun sin backend, mientras los detalles remotos conservan su ruta independiente.
- También se recorrieron las pantallas en el emulador: resultados con tres usuarios reales, Profile de `mariana.tech` con sus dos reseñas, selección de producto, publicación de una reseña, edición de su puntuación y eliminación confirmada. El estado final se comprobó por la interfaz y con `GET /users/1/reviews`.
- La base local `devicers` está desactualizada: le falta `users.firebase_uid`. La prueba aislada evita cambiar sus datos.
- El puerto 3000 está ocupado por otra aplicación. AppModule mantiene `http://10.0.2.2:3000/` como valor normal y admite `-PdevicersApiBaseUrl=http://10.0.2.2:3001/` para pruebas.

### Repetir la prueba en este equipo

Con PostgreSQL activo y ambos repositorios como carpetas hermanas, inicia un emulador desde Android Studio. Desde la raíz del repositorio Android, ejecuta `node scripts/start-test-api.mjs` en una terminal y déjala abierta. El script crea una base temporal y abre la API en el puerto 3001. En otra terminal, dentro de `DevicersApp`, ejecuta:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -PdevicersApiBaseUrl=http://10.0.2.2:3001/
```

Al terminar, pulsa Enter en la primera terminal. El script cierra la API y elimina la base temporal. La prueba de red se omite cuando se ejecutan las pruebas instrumentadas con la URL normal de la app.

Para recorrer las pantallas manualmente sin Firebase, instala una variante debug que abra directamente los resultados de usuarios:

```powershell
.\gradlew.bat :app:installDebug -PdevicersApiBaseUrl=http://10.0.2.2:3001/ -PdevicersTestStartDestination=profile-search-results
```

Inicia la app y visita Buscar usuarios → Profile, Crear reseña → elegir producto → publicar, y Perfil → reseña propia → menú de tres puntos → editar/eliminar. La propiedad `devicersTestStartDestination` solo se aplica a compilaciones debug; sin ella la app comienza en Splash como siempre. También admite `create` y `profile` para abrir esas pantallas directamente. La ruta desde Product ya está integrada con la tarea 4.

## Validación de la integración con tarea 4

Para ejecutar también la comprobación del grafo real de navegación:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:connectedDebugAndroidTest -PdevicersApiBaseUrl=http://10.0.2.2:3001/ -PdevicersTestStartDestination=profile-search-results
```

`IntegratedBackendFlowTest` recorre Profile, Product, publicación, detalle, edición y eliminación mediante los ViewModels y repositorios compartidos contra PostgreSQL temporal. Comprueba los IDs y la actualización de Product después de editar y eliminar; limpia la reseña temporal al finalizar. `IntegratedNavigationTest` comprueba en Compose el recorrido Profile → Review → Product → RateProduct con el grafo y Hilt reales. Las pruebas instrumentadas de integración solo se habilitan contra la URL aislada; la de navegación requiere además el destino debug indicado.

El PR #42 propone integrar `task/#6-7` en `develop`; todavía no se ha hecho el merge. Comparar contra `origin/task/#4` permite revisar los cambios propios de 6–7; comparar contra develop incluye también la dependencia task/#4 mientras esa rama no esté integrada allí.
