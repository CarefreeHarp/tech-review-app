package com.example.devicersapp.ui.screens.review.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/** Muestra una respuesta individual con su autor, fecha, contenido y acción. */
@Composable
fun ReplyItem(
    @StringRes authorResId: Int,
    @StringRes textResId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(authorResId),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = colorResource(R.color.text_primary_light)
        )
        Text(
            text = stringResource(R.string.review_reply_time),
            fontSize = 11.sp,
            color = colorResource(R.color.text_secondary_light)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(textResId),
            fontSize = 13.sp,
            color = colorResource(R.color.text_secondary_light)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.review_reply_action),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light)
        )
    }
}

/** Muestra una vista previa de una respuesta individual. */
@Composable
@Preview(showBackground = true)
fun ReplyItemPreview() {
    ReplyItem(R.string.review_reply_author_one, R.string.review_reply_text_one)
}
