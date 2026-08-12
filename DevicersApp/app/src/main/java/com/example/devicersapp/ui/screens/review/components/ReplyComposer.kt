package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Row(
        modifier = modifier
            .background(colorResource(R.color.surface_light))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.review_reply_placeholder),
                    fontSize = 13.sp,
                    color = colorResource(R.color.text_secondary_light)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.surface_secondary_light),
                focusedContainerColor = colorResource(R.color.surface_secondary_light),
                unfocusedBorderColor = colorResource(R.color.border_light),
                focusedBorderColor = colorResource(R.color.primary_yellow)
            )
        )
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(40.dp)
                .background(colorResource(R.color.primary_yellow), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.send_icon),
                contentDescription = stringResource(R.string.review_send),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/** Muestra una vista previa del compositor de respuesta. */
@Composable
@Preview(showBackground = true)
fun ReplyComposerPreview() { ReplyComposer() }
