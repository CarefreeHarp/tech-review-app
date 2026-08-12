package com.example.devicersapp.ui.utils.authentication

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/**
 * Renderiza el botón de acción principal compartido por las pantallas de autenticación.
 *
 * @param textResId Recurso de texto mostrado dentro del botón.
 * @param modifier Modificador aplicado al botón.
 */
@Composable
fun PrimaryButton(@StringRes textResId: Int, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primary_yellow),
            contentColor = colorResource(R.color.text_primary_light)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(textResId),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 19.sp
        )
    }
}

/**
 * Muestra el encabezado compartido de acceso y registro con la marca y el tema activo.
 *
 * @param modifier Modificador aplicado a la fila del encabezado.
 */
@Composable
fun AuthenticationHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo_claro),
            contentDescription = stringResource(R.string.devicers_logo_description),
            modifier = Modifier.size(width = 186.dp, height = 50.dp)
        )
        // El espacio flexible mantiene el estado del tema alineado a la derecha.
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.light_theme),
            color = colorResource(R.color.text_secondary_light),
            fontSize = 8.sp
        )
    }
}

/**
 * Muestra el título y la descripción de apoyo de una pantalla de autenticación.
 *
 * @param titleResId Recurso de texto del título.
 * @param descriptionResId Recurso de texto de la descripción.
 * @param modifier Modificador aplicado al contenedor.
 */
@Composable
fun ScreenTitle(
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(titleResId),
            color = colorResource(R.color.text_primary_light),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(descriptionResId),
            color = colorResource(R.color.text_secondary_light),
            fontSize = 15.sp,
            lineHeight = 15.sp
        )
    }
}

/**
 * Renderiza un campo visual de autenticación, con soporte opcional para contraseña.
 *
 * @param labelResId Recurso de texto de la etiqueta.
 * @param placeholderResId Recurso opcional del texto de ejemplo.
 * @param isPassword Indica si se debe mostrar la acción visual de contraseña.
 * @param modifier Modificador aplicado al contenedor del campo.
 */
@Composable
fun AuthenticationField(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(labelResId),
            color = colorResource(R.color.text_primary_light),
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = "",
            // El prototipo conserva un campo estático hasta conectar el estado del formulario.
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = placeholderResId?.let { stringResource(it) }.orEmpty(),
                    color = colorResource(R.color.text_secondary_light),
                    fontSize = 12.sp
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.hidden),
                            contentDescription = stringResource(R.string.show_password),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                null
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.text_primary_light),
                unfocusedTextColor = colorResource(R.color.text_primary_light),
                focusedContainerColor = colorResource(R.color.surface_secondary_light),
                unfocusedContainerColor = colorResource(R.color.surface_secondary_light),
                focusedBorderColor = colorResource(R.color.primary_yellow),
                unfocusedBorderColor = colorResource(R.color.border_light),
                cursorColor = colorResource(R.color.primary_yellow)
            )
        )
    }
}

/**
 * Muestra el texto que separa el formulario de las alternativas de acceso social.
 *
 * @param modifier Modificador aplicado al texto.
 */
@Composable
fun AuthenticationDividerText(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.continue_with),
        modifier = modifier,
        color = colorResource(R.color.text_secondary_light),
        fontSize = 10.sp
    )
}

/**
 * Renderiza un botón social con el ícono y la descripción proporcionados.
 *
 * @param iconResId Recurso del ícono social.
 * @param contentDescriptionResId Recurso de texto usado como descripción accesible.
 * @param modifier Modificador aplicado al botón.
 */
@Composable
fun SocialButton(
    @DrawableRes iconResId: Int,
    @StringRes contentDescriptionResId: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.surface_light),
            contentColor = colorResource(R.color.text_primary_light)
        )
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = stringResource(contentDescriptionResId),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Muestra las alternativas sociales de Google, Facebook y teléfono.
 *
 * @param modifier Modificador aplicado a la fila de acciones.
 */
@Composable
fun SocialButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SocialButton(R.drawable.google, R.string.google_sign_in, Modifier.weight(1f))
        SocialButton(R.drawable.facebook_app_symbol, R.string.facebook_sign_in, Modifier.weight(1f))
        SocialButton(R.drawable.phone_call, R.string.phone_sign_in, Modifier.weight(1f))
    }
}

/**
 * Muestra el mensaje sobre el estado de la cuenta y su acción visual asociada.
 *
 * @param textResId Recurso del mensaje principal.
 * @param actionResId Recurso de la acción mostrada como enlace.
 * @param modifier Modificador aplicado a la fila.
 */
@Composable
fun AuthenticationFooter(
    @StringRes textResId: Int,
    @StringRes actionResId: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(textResId),
            color = colorResource(R.color.text_primary_light),
            fontSize = 10.sp
        )
        TextButton(onClick = {}) {
            Text(
                text = stringResource(actionResId),
                color = colorResource(R.color.text_primary_light),
                fontSize = 11.sp
            )
        }
    }
}

/** Muestra una vista previa del botón principal de autenticación. */
@Composable
@Preview(showBackground = true)
fun PrimaryButtonPreview() {
    PrimaryButton(R.string.sign_in, Modifier.fillMaxWidth().height(48.dp))
}

/** Muestra una vista previa del encabezado de autenticación. */
@Composable
@Preview(showBackground = true)
fun AuthenticationHeaderPreview() {
    AuthenticationHeader()
}

/** Muestra una vista previa del título de inicio de sesión. */
@Composable
@Preview(showBackground = true)
fun ScreenTitlePreview() {
    ScreenTitle(R.string.sign_in_title, R.string.sign_in_description)
}

/** Muestra una vista previa de un campo de contraseña. */
@Composable
@Preview(showBackground = true)
fun AuthenticationFieldPreview() {
    AuthenticationField(R.string.password, R.string.password_placeholder, isPassword = true)
}

/** Muestra una vista previa del texto divisor. */
@Composable
@Preview(showBackground = true)
fun AuthenticationDividerTextPreview() {
    AuthenticationDividerText()
}

/** Muestra una vista previa de un botón social. */
@Composable
@Preview(showBackground = true)
fun SocialButtonPreview() {
    SocialButton(R.drawable.google, R.string.google_sign_in)
}

/** Muestra una vista previa de las acciones sociales. */
@Composable
@Preview(showBackground = true)
fun SocialButtonsPreview() {
    SocialButtons()
}

/** Muestra una vista previa del pie de autenticación. */
@Composable
@Preview(showBackground = true)
fun AuthenticationFooterPreview() {
    AuthenticationFooter(R.string.no_account, R.string.create_account)
}
