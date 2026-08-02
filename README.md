# Tech Review App

Aplicación móvil Android de reseñas de tecnología, creada para la asignatura de Computación Móvil de la Pontificia Universidad Javeriana.

La aplicación plantea una pequeña red social donde las personas pueden descubrir artículos tecnológicos, calificarlos y compartir reseñas con la comunidad.

## Diseño

El prototipo de interfaces de Devicers está disponible en [Figma](https://www.figma.com/design/l7mETjAKWkyAFwtx1VWmIX/Untitled?node-id=1-484&t=p0lttPj7kJNiSUG2-1).

## Sprint 1

- **Asignatura:** Computación Móvil — Pontificia Universidad Javeriana.
- **Profesor:** Juan Sebastián Angarita Torres.
- **Fecha:** 29 de julio de 2026.
- **Equipo:** Daniel Felipe Ramírez Vargas, Edwin Esteban Barreto Gaitán y Guillermo Andrés Aponte Cárdenas.

### Entregables

- Documento de requisitos funcionales.
- Diagrama entidad-relación de la base de datos.
- Diagrama de clases (POO).
- Logo, paleta de colores y definición del nombre de la aplicación.
- Cuatro pantallas iniciales.

## Requisitos funcionales

- **RF-01 a RF-04 — Autenticación:** crear una cuenta con correo electrónico, nombre de usuario y contraseña; iniciar sesión; y restringir las funcionalidades a usuarios registrados.
- **RF-05 y RF-06 — Feed:** mostrar las reseñas de las cuentas seguidas, ordenadas de la más reciente a la más antigua.
- **RF-08 a RF-11 — Descubrimiento de artículos:** filtrar por subcategorías, buscar por nombre, seleccionar un artículo desde el feed o los resultados y ver sus datos principales.
- **Reseñas de la comunidad:** mostrar reseñas de otros usuarios sobre un artículo y permitir acceder al perfil de su autor.
- **RF-12 a RF-16 — Calificaciones y reseñas:** calificar artículos con valores enteros entre 0 y 5, escribir un análisis, limitar a una reseña por usuario y artículo, y permitir su posterior edición.
- **RF-17 — Comentarios:** comentar los análisis de otros usuarios, incluso si no se les sigue; mostrar los comentarios e identificar a su autor y fecha de publicación.
- **Seguimiento:** permitir seguir a otros usuarios.
- **RF-18 a RF-20 — Perfil:** modificar datos y foto de perfil, eliminar la cuenta y consultar todos los artículos calificados.
- **RF-21 a RF-23 — Notificaciones:** avisar sobre nuevos seguidores, comentarios en reseñas propias y reacciones "me gusta" en reseñas propias.
- **RF-24 — Lectura de reseñas:** seleccionar una reseña del feed para consultar su contenido completo.

## Alcance técnico

El proyecto se desarrollará con arquitectura MVVM y patrones como Repository e inyección de dependencias. Se contempla una API REST con base de datos SQL, Firebase Authentication y Firestore, además de pruebas automatizadas y notificaciones mediante Firebase.

## Equipo

Proyecto académico — Ingeniería de Sistemas, Pontificia Universidad Javeriana (Bogotá).
