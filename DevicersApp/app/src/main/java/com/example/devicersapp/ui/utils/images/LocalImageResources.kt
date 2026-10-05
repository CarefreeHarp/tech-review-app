package com.example.devicersapp.ui.utils.images

import androidx.annotation.DrawableRes
import com.example.devicersapp.R

// El backend guarda el nombre del recurso (por ejemplo "profile_avatar_01") porque los ids de R cambian
// en cada compilación. Se usa un mapa explícito en lugar de Resources.getIdentifier para que el compilador
// valide cada recurso y la reducción de recursos de R8 no elimine imágenes que solo se piden por nombre.
private val localImageResIds = mapOf(
    "device_00" to R.drawable.device_00,
    "device_01" to R.drawable.device_01,
    "device_02" to R.drawable.device_02,
    "device_03" to R.drawable.device_03,
    "device_04" to R.drawable.device_04,
    "device_05" to R.drawable.device_05,
    "device_06" to R.drawable.device_06,
    "device_07" to R.drawable.device_07,
    "device_08" to R.drawable.device_08,
    "device_09" to R.drawable.device_09,
    "profile_avatar_00" to R.drawable.profile_avatar_00,
    "profile_avatar_01" to R.drawable.profile_avatar_01,
    "profile_avatar_02" to R.drawable.profile_avatar_02,
    "profile_avatar_03" to R.drawable.profile_avatar_03,
    "profile_avatar_04" to R.drawable.profile_avatar_04,
    "profile_avatar_05" to R.drawable.profile_avatar_05
)

/**
 * Traduce el valor de imagen que envía el backend a un drawable local.
 *
 * @param imageName Nombre del recurso guardado en `image_url` o `profile_image_url`.
 * @return El drawable correspondiente, o `null` si el valor está vacío o es una URL remota.
 */
@DrawableRes
fun localImageResIdFor(imageName: String?): Int? = imageName?.let(localImageResIds::get)
