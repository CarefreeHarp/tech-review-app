package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.time.Instant
/** Conserva la precisión de nanosegundos de Firestore al entregar fechas ISO a la aplicación. */
internal fun Timestamp?.isoText(): String? = this?.let { Instant.ofEpochSecond(seconds, nanoseconds.toLong()).toString() }
/** Rechaza un ID o contador corrupto en lugar de truncarlo al convertirlo a Int. */
internal fun DocumentSnapshot.integer(field: String): Int {
    val number = checkNotNull(getLong(field)) { "$field ausente en $reference" }
    check(number in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) { "$field fuera de rango" }
    return number.toInt()
}
internal fun DocumentSnapshot.numericDocumentId(): Int = id.toInt().also { check(it > 0) }

/** Decodificación de valores separada del SDK de consultas para poder comprobar los mapeos reales. */
internal fun Map<String, Any?>.integer(field: String): Int {
    val value = get(field)
    check(value is Long || value is Int) { "$field no es un entero documental" }
    val number = (value as Number).toLong()
    check(number in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) { "$field fuera de rango" }
    return number.toInt()
}
internal fun Map<String, Any?>.optionalInteger(field: String): Int? = if (get(field) == null) null else integer(field)
internal fun Map<String, Any?>.getString(field: String): String? = get(field)?.let {
    check(it is String) { "$field no es texto" }; it
}
internal fun Map<String, Any?>.getTimestamp(field: String): Timestamp? = get(field)?.let {
    check(it is Timestamp) { "$field no es Timestamp" }; it
}
internal fun Map<String, Any?>.text(field: String): String = checkNotNull(getString(field)) { "$field ausente" }
internal fun Map<String, Any?>.flag(field: String): Boolean = get(field).let {
    check(it is Boolean) { "$field no es booleano" }; it
}
