package com.example.devicersapp.ui.screens.request_product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Ofrece adjuntar una fotografía al producto que se está solicitando.
 *
 * @param modifier Modificador aplicado al área de subida.
 * @param onUploadClick Acción solicitada al elegir una fotografía.
 */
@Composable
fun ProductPhotoUploader(
    modifier: Modifier = Modifier,
    onUploadClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(colors.surface, RoundedCornerShape(16.dp))
            .clickable { onUploadClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        // El círculo claro con el signo de acción invita a tocar toda el área.
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(colors.background, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.create_review_icon),
                contentDescription = stringResource(R.string.request_product_add_photo),
                modifier = Modifier.size(22.dp),
                tint = colors.primaryText
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(R.string.request_product_upload),
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = stringResource(R.string.request_product_upload_hint),
            color = colors.textSecondary,
            style = CardMetadataText
        )
    }
}

/** Muestra una vista previa del área para adjuntar la fotografía del producto. */
@Composable
@Preview(showBackground = true)
fun ProductPhotoUploaderPreview() {
    DevicersAppTheme {
        ProductPhotoUploader(modifier = Modifier.padding(16.dp))
    }
}
