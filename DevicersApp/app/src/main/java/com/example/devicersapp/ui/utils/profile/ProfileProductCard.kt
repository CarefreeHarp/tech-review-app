package com.example.devicersapp.ui.utils.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra un producto ya calificado dentro de la cuadrícula del perfil.
 *
 * @param review Reseña creada por el perfil y representada en la tarjeta.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onClick Acción solicitada al abrir el producto.
 */
@Composable
fun ProfileProductCard(
    review: ReviewContent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .background(colors.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(review.productImageResId),
            contentDescription = stringResource(R.string.review_product_image),
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(review.productNameResId),
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        RatingStars(rating = review.rating)
    }
}

/** Muestra una vista previa de una tarjeta de producto calificado. */
@Composable
@Preview(showBackground = true)
fun ProfileProductCardPreview() {
    DevicersAppTheme {
        ProfileProductCard(
            review = LocalReviewProvider.reviews.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
