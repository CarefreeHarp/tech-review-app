package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/** Muestra el compositor fijo para responder a la reseña actual. */
@Composable
fun ReplyComposer(modifier: Modifier = Modifier) {
    var replyText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .background(colorResource(R.color.surface_light))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = replyText,
            onValueChange = { replyText = it },
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .background(
                    colorResource(R.color.background_light),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.border_light),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = colorResource(R.color.text_primary_light),
                fontSize = 13.sp
            ),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (replyText.isEmpty()) {
                        Text(
                            text = stringResource(R.string.review_reply_placeholder),
                            fontSize = 13.sp,
                            color = colorResource(R.color.text_secondary_light)
                        )
                    }
                    innerTextField()
                }
            }
        )
        Image(
            painter = painterResource(R.drawable.send_icon),
            contentDescription = stringResource(R.string.review_send),
            modifier = Modifier
                .padding(start = 16.dp)
                .size(28.dp)
        )
    }
}

/** Muestra una vista previa del compositor de respuesta. */
@Composable
@Preview(showBackground = true)
fun ReplyComposerPreview() { ReplyComposer() }
