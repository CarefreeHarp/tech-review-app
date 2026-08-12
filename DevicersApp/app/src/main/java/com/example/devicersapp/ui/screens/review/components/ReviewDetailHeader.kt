package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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

/** Muestra el encabezado de detalle con acción de regreso, título y marcador. */
@Composable
fun ReviewDetailHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.review_back),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(stringResource(R.string.review_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.text_primary_light))
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.bookmark_icon),
            contentDescription = stringResource(R.string.review_save),
            modifier = Modifier.size(20.dp)
        )
    }
}

/** Muestra una vista previa del encabezado de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewDetailHeaderPreview() { ReviewDetailHeader() }
