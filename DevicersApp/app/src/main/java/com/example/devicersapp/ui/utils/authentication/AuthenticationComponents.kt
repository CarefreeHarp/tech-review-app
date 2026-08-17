package com.example.devicersapp.ui.utils.authentication

import com.example.devicersapp.ui.theme.LocalDevicersColors

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.AuthenticationFieldLabelText
import com.example.devicersapp.ui.theme.AuthenticationSupportText

/**
 * Renderiza el botón de acción principal compartido por las pantallas de autenticación.
 *
 * @param textResId Recurso de texto mostrado dentro del botón.
 * @param modifier Modificador aplicado al botón.
 * @param onClick Acción solicitada al pulsar el botón.
 */
@Composable
fun PrimaryButton(
    @StringRes textResId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalDevicersColors.current.primaryYellow,
            contentColor = LocalDevicersColors.current.textPrimary
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
            color = LocalDevicersColors.current.textPrimary,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(descriptionResId),
            color = LocalDevicersColors.current.textSecondary,
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
 * @param value Texto actual controlado por la pantalla que usa el campo.
 * @param onValueChange Acción que solicita actualizar el texto del campo.
 * @param isPassword Indica si se debe mostrar la acción visual de contraseña.
 * @param isPasswordVisible Indica si el contenido de la contraseña está visible.
 * @param onPasswordVisibilityChange Acción que solicita alternar la visibilidad de la contraseña.
 * @param modifier Modificador aplicado al contenedor del campo.
 */
@Composable
fun AuthenticationField(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Se reutiliza el carácter especial definido para el placeholder como máscara de contraseña.
    val passwordMask = stringResource(R.string.password_placeholder).first()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(labelResId),
            color = LocalDevicersColors.current.textPrimary,
            style = AuthenticationFieldLabelText
        )
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = placeholderResId?.let { stringResource(it) }.orEmpty(),
                    color = LocalDevicersColors.current.textSecondary,
                    fontSize = 12.sp
                )
            },
            trailingIcon = if (isPassword) {
                {
                    // La pantalla propietaria cambia el estado para sincronizar campos de contraseña relacionados.
                    IconButton(onClick = onPasswordVisibilityChange) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordVisible) R.drawable.eye else R.drawable.hidden
                            ),
                            contentDescription = stringResource(
                                if (isPasswordVisible) R.string.hide_password else R.string.show_password
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                null
            },
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation(mask = passwordMask)
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalDevicersColors.current.textPrimary,
                unfocusedTextColor = LocalDevicersColors.current.textPrimary,
                focusedContainerColor = LocalDevicersColors.current.surfaceSecondary,
                unfocusedContainerColor = LocalDevicersColors.current.surfaceSecondary,
                focusedBorderColor = LocalDevicersColors.current.primaryYellow,
                unfocusedBorderColor = LocalDevicersColors.current.border,
                cursorColor = LocalDevicersColors.current.primaryYellow
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
        color = LocalDevicersColors.current.textSecondary,
        style = AuthenticationSupportText
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
            containerColor = LocalDevicersColors.current.surface,
            contentColor = LocalDevicersColors.current.textPrimary
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
            color = LocalDevicersColors.current.textPrimary,
            style = AuthenticationSupportText
        )
        TextButton(onClick = {}) {
            Text(
                text = stringResource(actionResId),
                color = LocalDevicersColors.current.textPrimary,
                style = AuthenticationSupportText
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
    AuthenticationField(
        labelResId = R.string.password,
        placeholderResId = R.string.password_placeholder,
        value = "",
        onValueChange = {},
        isPassword = true
    )
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
