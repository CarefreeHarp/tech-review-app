package com.example.devicersapp.ui.screens.notifications

import androidx.annotation.DrawableRes
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.NotificationContent

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

/**
 * Selecciona uno de los seis avatares locales de forma estable para cada notificación de ejemplo.
 *
 * El identificador produce siempre el mismo recurso, para evitar que un perfil cambie de imagen
 * durante una recomposición de la lista.
 */
@DrawableRes
private fun profileAvatarResId(notificationId: String): Int = when (notificationId.hashCode().and(Int.MAX_VALUE) % 6) {
    0 -> R.drawable.profile_avatar_00
    1 -> R.drawable.profile_avatar_01
    2 -> R.drawable.profile_avatar_02
    3 -> R.drawable.profile_avatar_03
    4 -> R.drawable.profile_avatar_04
    else -> R.drawable.profile_avatar_05
}

/**
 * Provee los datos de ejemplo quemados usados para representar las notificaciones locales.
 *
 * @param currentTimeMillis Referencia temporal que permite generar instantes relativos coherentes.
 */
fun notificationsSampleData(currentTimeMillis: Long): List<Pair<String, List<NotificationContent>>> = listOf(
    "HOY" to listOf(
        NotificationContent("today-camila", profileAvatarResId("today-camila"), "@camila.vargas", "Le gustó tu reseña", "Sony WH-1000XM5 · Cancelación de ruido", currentTimeMillis - 5 * MINUTE_IN_MILLIS),
        NotificationContent("today-david", profileAvatarResId("today-david"), "@davidtechea", "Le gustó tu comentario", "“La autonomía del Pixel 9 Pro sí mejora con el uso.”", currentTimeMillis - HOUR_IN_MILLIS),
        NotificationContent("today-mateo", profileAvatarResId("today-mateo"), "@mateo.dev", "Le gustó tu reseña", "Keychron K2 Pro · Switches brown", currentTimeMillis - 2 * HOUR_IN_MILLIS),
        NotificationContent("today-lina", profileAvatarResId("today-lina"), "@lina.labs", "Respondió a tu comentario", "“En exteriores el brillo del Galaxy Tab S10 se mantiene muy bien.”", currentTimeMillis - 3 * HOUR_IN_MILLIS),
        NotificationContent("today-valeria", profileAvatarResId("today-valeria"), "@valeriagomez", "Le gustó tu comentario", "“Por ese precio, los Redmi Buds 6 Pro son una compra sólida.”", currentTimeMillis - 4 * HOUR_IN_MILLIS),
        NotificationContent("today-gamer", profileAvatarResId("today-gamer"), "@julianplays", "Comenzó a seguirte", "Publica reseñas de consolas y periféricos", currentTimeMillis - 5 * HOUR_IN_MILLIS, showFollowAction = true),
        NotificationContent("today-daniel", profileAvatarResId("today-daniel"), "@daniel.tech", "Le gustó tu reseña", "LG UltraGear 34GS95QE · Monitor OLED", currentTimeMillis - 6 * HOUR_IN_MILLIS),
        NotificationContent("today-sofia", profileAvatarResId("today-sofia"), "@sofia.review", "Respondió a tu comentario", "“Con brillo automático, mi iPhone 16 me está dando casi siete horas de pantalla.”", currentTimeMillis - 8 * HOUR_IN_MILLIS)
    ),
    "AYER" to listOf(
        NotificationContent("yesterday-fernando", profileAvatarResId("yesterday-fernando"), "@fernando.reviews", "Comenzó a seguirte", "Compara equipos de audio para estudio en casa", currentTimeMillis - DAY_IN_MILLIS, showFollowAction = true),
        NotificationContent("yesterday-pablo", profileAvatarResId("yesterday-pablo"), "@pablo.gadgets", "Le gustó tu reseña", "Garmin Venu 3 · Seguimiento de sueño", currentTimeMillis - DAY_IN_MILLIS - 2 * HOUR_IN_MILLIS),
        NotificationContent("yesterday-natalia", profileAvatarResId("yesterday-natalia"), "@nataliaframes", "Le gustó tu comentario", "“El modo retrato del Xiaomi 14T mejora bastante cuando hay buena luz.”", currentTimeMillis - DAY_IN_MILLIS - 4 * HOUR_IN_MILLIS),
        NotificationContent("yesterday-valentina", profileAvatarResId("yesterday-valentina"), "@vale.codes", "Respondió a tu comentario", "“También noté menos calor al editar video después de la última actualización.”", currentTimeMillis - DAY_IN_MILLIS - 6 * HOUR_IN_MILLIS),
        NotificationContent("yesterday-andres", profileAvatarResId("yesterday-andres"), "@andres.android", "Comenzó a seguirte", "Reseña celulares Android de gama media", currentTimeMillis - DAY_IN_MILLIS - 8 * HOUR_IN_MILLIS, showFollowAction = true),
        NotificationContent("yesterday-juan", profileAvatarResId("yesterday-juan"), "@juan.review", "Le gustó tu reseña", "iPad Air M3 · Notas y edición ligera", currentTimeMillis - DAY_IN_MILLIS - 10 * HOUR_IN_MILLIS),
        NotificationContent("yesterday-ana", profileAvatarResId("yesterday-ana"), "@ana.tech", "Le gustó tu comentario", "“El acabado azul del Nothing Phone (3a) se ve mejor en persona.”", currentTimeMillis - DAY_IN_MILLIS - 12 * HOUR_IN_MILLIS)
    ),
    "ANTERIORES" to listOf(
        NotificationContent("previous-miguel", profileAvatarResId("previous-miguel"), "@miguel.audio", "Le gustó tu reseña", "Samsung Galaxy S25 · Cámara nocturna", currentTimeMillis - 3 * DAY_IN_MILLIS),
        NotificationContent("previous-paula", profileAvatarResId("previous-paula"), "@paula.mobile", "Le gustó tu comentario", "“Con dos días de uso, la batería del Moto Edge 50 sigue llegando al final de la jornada.”", currentTimeMillis - 5 * DAY_IN_MILLIS),
        NotificationContent("previous-luisa", profileAvatarResId("previous-luisa"), "@luisa.g", "Respondió a tu comentario", "“Probé ese mismo router TP-Link y el alcance mejoró bastante en mi apartamento.”", currentTimeMillis - 6 * DAY_IN_MILLIS),
        NotificationContent("previous-carlos", profileAvatarResId("previous-carlos"), "@carlos.dev", "Comenzó a seguirte", "Comparte configuraciones de escritorios para programación", currentTimeMillis - 7 * DAY_IN_MILLIS, showFollowAction = true),
        NotificationContent("previous-reviewhub", profileAvatarResId("previous-reviewhub"), "@reviewhub", "Le gustó tu reseña", "Fujifilm X100VI · Cámara compacta", currentTimeMillis - 8 * DAY_IN_MILLIS),
        NotificationContent("previous-natalia", profileAvatarResId("previous-natalia"), "@natalia.tech", "Le gustó tu comentario", "“La carga de 80 W del OnePlus 13R sí hace diferencia para el día a día.”", currentTimeMillis - 9 * DAY_IN_MILLIS),
        NotificationContent("previous-esteban", profileAvatarResId("previous-esteban"), "@esteban.r", "Respondió a tu comentario", "“Gracias por explicar lo del panel IPS; ahora tengo más claro cuál elegir.”", currentTimeMillis - 10 * DAY_IN_MILLIS),
        NotificationContent("previous-maria", profileAvatarResId("previous-maria"), "@maria.dev", "Le gustó tu reseña", "Soundcore Liberty 4 NC · Micrófonos", currentTimeMillis - 11 * DAY_IN_MILLIS),
        NotificationContent("previous-kevin", profileAvatarResId("previous-kevin"), "@kevin.tech", "Comenzó a seguirte", "Publica comparativas de laptops para la universidad", currentTimeMillis - 12 * DAY_IN_MILLIS, showFollowAction = true),
        NotificationContent("previous-laura", profileAvatarResId("previous-laura"), "@laura.reviews", "Le gustó tu comentario", "“Después de una semana, el agarre del Logitech MX Master 3S sigue siendo muy cómodo.”", currentTimeMillis - 14 * DAY_IN_MILLIS),
        NotificationContent("previous-felipe", profileAvatarResId("previous-felipe"), "@felipe.gadget", "Le gustó tu reseña", "ASUS ROG Ally X · Consola portátil", currentTimeMillis - 16 * DAY_IN_MILLIS)
    )
)
