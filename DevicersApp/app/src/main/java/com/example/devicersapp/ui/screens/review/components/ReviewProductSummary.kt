package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.devicersapp.R
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el producto asociado a la reseña.
 */
@Composable
fun ReviewProductSummary(
    product: ProductDto,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            placeholder = painterResource(
                R.drawable.device_00
            ),
            error = painterResource(
                R.drawable.device_00
            ),
            modifier = Modifier
                .width(136.dp)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(16.dp)
                ),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.width(18.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = product.model ?: "Sin modelo",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }
    }
}