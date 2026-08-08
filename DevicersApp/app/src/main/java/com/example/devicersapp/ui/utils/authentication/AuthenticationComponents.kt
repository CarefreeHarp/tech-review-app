package com.example.devicersapp.ui.utils.authentication

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.LogoApp

/** Renderiza el botón de acción amarillo compartido por las pantallas de autenticación. */
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

/** Muestra el encabezado compacto compartido por inicio de sesión y creación de cuenta. */
@Composable
fun AuthenticationHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoApp(modifier = Modifier.size(42.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            color = colorResource(R.color.text_primary_light),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        // Usa el espacio disponible para mantener la etiqueta del tema alineada a la derecha.
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.light_theme),
            color = colorResource(R.color.text_secondary_light),
            fontSize = 8.sp
        )
    }
}

/** Muestra el título y la descripción de apoyo de una pantalla de autenticación. */
@Composable
fun ScreenTitle(@StringRes title: Int, @StringRes description: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = stringResource(title), color = colorResource(R.color.text_primary_light), fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = stringResource(description), color = colorResource(R.color.text_secondary_light), fontSize = 15.sp, lineHeight = 15.sp)
    }
}

/**
 * Renderiza un campo de autenticación delineado, únicamente visual.
 * El callback de valor vacío es obligatorio para [OutlinedTextField], pero todavía no guarda texto.
 */
@Composable
fun AuthenticationField(
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = stringResource(label), color = colorResource(R.color.text_primary_light), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = "",
            // Material exige este callback; dejarlo vacío conserva el prototipo estático.
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = placeholder?.let { stringResource(it) }.orEmpty(),
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

/** Muestra el texto que separa la autenticación principal de las opciones sociales. */
@Composable
fun AuthenticationDividerText(modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.continue_with), modifier = modifier, color = colorResource(R.color.text_secondary_light), fontSize = 10.sp)
}

/** Muestra los botones sociales de Google, Facebook y teléfono del prototipo. */
@Composable
fun SocialButtons(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SocialButton(R.drawable.google, R.string.google_sign_in, Modifier.weight(1f))
        SocialButton(R.drawable.facebook_app_symbol, R.string.facebook_sign_in, Modifier.weight(1f))
        SocialButton(R.drawable.phone_call, R.string.phone_sign_in, Modifier.weight(1f))
    }
}

/** Renderiza un botón social estático con el ícono y la descripción proporcionados. */
@Composable
fun SocialButton(@DrawableRes iconResId: Int, @StringRes contentDescriptionResId: Int, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.surface_light),
            contentColor = colorResource(R.color.text_primary_light)
        )
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(iconResId),
            contentDescription = stringResource(contentDescriptionResId),
            modifier = Modifier.size(20.dp)
        )
    }
}

/** Muestra el mensaje estático sobre el estado de la cuenta y su enlace visual. */
@Composable
fun AuthenticationFooter(@StringRes textResId: Int, @StringRes actionResId: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(textResId), color = colorResource(R.color.text_primary_light), fontSize = 10.sp)
        TextButton(onClick = {}) {
            Text(text = stringResource(actionResId), color = colorResource(R.color.text_primary_light), fontSize = 11.sp)
        }
    }
}
