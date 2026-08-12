package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

/** Muestra el contenido principal de una reseña y sus acciones de interacción. */
@Composable
fun ReviewDetailCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.surface_light), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(36.dp).background(colorResource(R.color.surface_secondary_light), CircleShape), Alignment.Center) {
                Text(stringResource(R.string.review_author_initial), fontWeight = FontWeight.Bold, color = colorResource(R.color.text_primary_light))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.review_author), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorResource(R.color.text_primary_light))
                Text(stringResource(R.string.review_time), fontSize = 11.sp, color = colorResource(R.color.text_secondary_light))
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.review_rating), color = colorResource(R.color.primary_yellow), fontSize = 16.sp)
        Spacer(Modifier.height(25.dp))
        Text(stringResource(R.string.review_detail_text), fontSize = 14.sp, lineHeight = 19.sp, color = colorResource(R.color.text_secondary_light))
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.like_icon),
                    contentDescription = stringResource(R.string.review_like),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.review_like_count, 128), fontWeight = FontWeight.Black, fontSize = 11.sp, color = colorResource(R.color.text_secondary_light))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.send_icon),
                    contentDescription = stringResource(R.string.review_send),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(20.dp))
                Image(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = stringResource(R.string.review_save),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Muestra una vista previa de la tarjeta de detalle de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewDetailCardPreview() { ReviewDetailCard() }
