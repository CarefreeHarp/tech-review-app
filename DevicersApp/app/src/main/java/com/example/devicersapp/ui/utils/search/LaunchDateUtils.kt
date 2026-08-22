package com.example.devicersapp.ui.utils.search

/**
 * Conserva únicamente los ocho dígitos de una fecha y los muestra con el formato DD / MM / AAAA.
 *
 * @param input Contenido recibido desde el campo de fecha.
 * @return Fecha parcial o completa sin caracteres no numéricos.
 */
fun formatLaunchDate(input: String): String {
    val digits = input.filter(Char::isDigit).take(8)
    return buildString {
        digits.forEachIndexed { index, digit ->
            if (index == 2 || index == 4) append(" / ")
            append(digit)
        }
    }
}

/**
 * Comprueba que una fecha completa tenga un mes y un día válidos, incluidos los años bisiestos.
 *
 * @param formattedDate Fecha con el formato DD / MM / AAAA.
 * @return `true` solo si la fecha contiene ocho dígitos y existe en el calendario gregoriano.
 */
fun isValidLaunchDate(formattedDate: String): Boolean {
    val digits = formattedDate.filter(Char::isDigit)
    if (digits.length != 8) return false

    val day = digits.substring(0, 2).toInt()
    val month = digits.substring(2, 4).toInt()
    val year = digits.substring(4, 8).toInt()
    val maximumDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> return false
    }

    return day in 1..maximumDays
}

/**
 * Determina si un año es bisiesto según las reglas del calendario gregoriano.
 *
 * @param year Año de cuatro dígitos recibido desde el campo de fecha.
 * @return `true` cuando febrero tiene veintinueve días.
 */
private fun isLeapYear(year: Int): Boolean = year % 400 == 0 || year % 4 == 0 && year % 100 != 0
