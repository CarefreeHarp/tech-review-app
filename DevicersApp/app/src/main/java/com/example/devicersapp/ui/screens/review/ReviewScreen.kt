package com.example.devicersapp.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.review.components.ReplyComposer
import com.example.devicersapp.ui.screens.review.components.ReplyList
import com.example.devicersapp.ui.screens.review.components.ReviewDetailCard
import com.example.devicersapp.ui.screens.review.components.ReviewDetailHeader
import com.example.devicersapp.ui.screens.review.components.ReviewProductSummary

/** Configura el fondo y la estructura principal de la pantalla de detalle de reseña. */
@Composable
fun ReviewScreen(modifier: Modifier = Modifier) {
    ReviewScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/** Ensambla el producto, la reseña, las respuestas y el compositor de respuesta. */
@Composable
fun ReviewScreenContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ReviewDetailHeader()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {
            ReviewProductSummary()
            ReviewDetailCard(modifier = Modifier.padding(top = 12.dp))
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            ReplyList(modifier = Modifier.padding(vertical = 20.dp))
        }
        ReplyComposer(modifier = Modifier.fillMaxWidth())
    }
}

/** Muestra una vista previa de la pantalla completa de detalle de reseña. */
@Composable
@Preview(showBackground = true)
fun ReviewScreenPreview() {
    ReviewScreen()
}
