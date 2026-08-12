package com.example.devicersapp.ui.screens.search.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/**
 * Muestra el control visual de calificación mínima seleccionado en cuatro estrellas.
 *
 * @param modifier Modificador aplicado al control.
 */
@Composable
fun RatingSlider(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    color = colorResource(R.color.border_light),
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(4.dp)
                    .background(
                        color = colorResource(R.color.primary_yellow),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(12.dp)
                    .background(
                        color = colorResource(R.color.primary_yellow),
                        shape = RoundedCornerShape(50)
                    )
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.rating_zero), fontSize = 7.sp)
            Text(stringResource(R.string.rating_one), fontSize = 7.sp)
            Text(stringResource(R.string.rating_two), fontSize = 7.sp)
            Text(stringResource(R.string.rating_three), fontSize = 7.sp)
            Text(stringResource(R.string.rating_four), fontSize = 7.sp)
            Text(stringResource(R.string.rating_five), fontSize = 7.sp)
        }
    }
}

/** Muestra una vista previa del control de calificación. */
@Composable
@Preview(showBackground = true)
fun RatingSliderPreview() {
    RatingSlider(modifier = Modifier.padding(16.dp))
}
