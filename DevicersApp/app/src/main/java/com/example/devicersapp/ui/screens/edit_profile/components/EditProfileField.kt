package com.example.devicersapp.ui.screens.edit_profile.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.AuthenticationFieldLabelText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Renderiza un campo del perfil controlado por el padre, con opción de lectura y varias líneas. */
@Composable
fun EditProfileField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    multiline: Boolean = false
) {
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(stringResource(labelResId), style = AuthenticationFieldLabelText, color = colors.textPrimary)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(value = value, onValueChange = onValueChange, readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium, singleLine = !multiline, minLines = if (multiline) 4 else 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.textPrimary, unfocusedTextColor = colors.textPrimary,
                focusedContainerColor = colors.surface, unfocusedContainerColor = colors.surface,
                focusedBorderColor = colors.primary, unfocusedBorderColor = colors.border, cursorColor = colors.primary))
    }
}

/** Previsualiza un campo con el estilo compartido de formularios. */
@Preview(showBackground = true)
@Composable
fun EditProfileFieldPreview() {
    DevicersAppTheme { EditProfileField(R.string.username, stringResource(R.string.username_placeholder), {}) }
}
