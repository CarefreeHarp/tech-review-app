package com.example.devicersapp.ui.screens.feed.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

/**
 * Muestra una reseña de un producto dentro del Feed.
 *
 * Permite configurar el tipo de elemento, su imagen, el usuario,
 * la cantidad de likes y el tiempo transcurrido desde su publicación.
 *
 * @param elementTypeResId Recurso del tipo de elemento asociado a la reseña.
 * @param imageResId Recurso drawable utilizado como imagen del elemento.
 * @param usernameResId Recurso del usuario que publicó la reseña.
 * @param reviewResId Recurso del texto de la reseña.
 * @param likes Cantidad de likes que tiene la reseña.
 * @param timeAgoResId Recurso del tiempo transcurrido desde la publicación.
 * @param modifier Permite modificar el diseño externo del componente.
 */
@Composable
fun ReviewBox(
    @StringRes elementTypeResId: Int,
    @DrawableRes imageResId: Int,
    @StringRes usernameResId: Int,
    @StringRes reviewResId: Int,
    likes: Int,
    @StringRes timeAgoResId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.surface_light),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(imageResId),
                contentDescription = stringResource(R.string.review_product_image),
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(usernameResId),
                    color = colorResource(R.color.text_secondary_light),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(elementTypeResId),
                    color = colorResource(R.color.text_primary_light),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.review_rating),
                    color = colorResource(R.color.primary_yellow),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(timeAgoResId),
                color = colorResource(R.color.text_secondary_light),
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(reviewResId),
            color = colorResource(R.color.text_secondary_light),
            fontSize = 10.sp,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.like_icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = likes.toString(),
                color = colorResource(R.color.text_secondary_light),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.send_icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(40.dp))
            Image(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ReviewBoxPreview() {
    ReviewBox(elementTypeResId = R.string.feed_product_phone,
        imageResId = R.drawable.iphone,
        usernameResId = R.string.feed_user_phone,
        reviewResId = R.string.feed_review_phone,
        likes = 125,
        timeAgoResId = R.string.feed_time_two_days
    )
}
