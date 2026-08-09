package com.example.devicersapp.ui.screens.feed.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.devicersapp.R
import com.example.devicersapp.ui.utils.LogoApp

/**
 * Muestra el encabezado centrado utilizado en la pantalla principal del Feed.
 *
 * Combina el logo de Devicers con el nombre de la aplicación y mantiene
 * ambos elementos agrupados para que puedan centrarse como una sola unidad.
 */
@Composable
fun FeedHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoApp(
            modifier = Modifier.size(42.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.app_name),
            color = colorResource(R.color.text_primary_light),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

/**
 * Muestra una vista previa del encabezado centrado del Feed.
 */
@Composable
@Preview(showBackground = true)
fun FeedHeaderPreview() {
    FeedHeader()
}