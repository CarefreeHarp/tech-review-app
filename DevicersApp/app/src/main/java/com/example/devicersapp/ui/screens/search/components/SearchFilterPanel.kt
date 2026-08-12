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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.devicersapp.ui.utils.navigation.FilterChip
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Muestra el panel visual con todos los filtros disponibles para la búsqueda.
 *
 * El componente representa únicamente el diseño del formulario y no
 * implementa todavía cambios de estado ni aplicación de filtros.
 *
 * @param modifier Permite modificar el diseño externo del panel.
 */
@Composable
fun SearchFilterPanel(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .background(
                color = colorResource(R.color.surface_secondary_light),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.search_filters),
                color = colorResource(R.color.text_primary_light),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.clear_filters),
                color = colorResource(R.color.text_secondary_light),
                fontSize = 8.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        FilterLabel(R.string.brand)

        SearchBar(
            placeholder = R.string.brand_placeholder,
            showSearchIcon = false,
            backgroundColor = R.color.background_light
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterLabel(R.string.product_name)

        SearchBar(
            placeholder = R.string.product_name_placeholder,
            showSearchIcon = false,
            backgroundColor = R.color.background_light
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterLabel(R.string.launch_date)

        SearchBar(
            placeholder = R.string.launch_date_placeholder,
            showSearchIcon = false,
            backgroundColor = R.color.background_light
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterLabel(R.string.category)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                text = R.string.all,
                selected = true
            )

            FilterChip(
                text = R.string.cellphones,
                selected = false
            )

            FilterChip(
                text = R.string.audio,
                selected = false
            )

            FilterChip(
                text = R.string.computers,
                selected = false
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterLabel(
                textResId = R.string.minimum_rating,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(R.string.rating_four_or_more),
                color = colorResource(R.color.text_primary_light),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }

        RatingSlider()

        Spacer(modifier = Modifier.height(12.dp))

        FilterLabel(R.string.sort_by)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                text = R.string.most_recent,
                selected = true,
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                text = R.string.best_rated,
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .background(
                    color = colorResource(R.color.primary_yellow),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                color = colorResource(R.color.text_primary_light),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Muestra la etiqueta superior de un campo de filtro.
 *
 * @param textResId Recurso de texto utilizado para la etiqueta.
 * @param modifier Permite modificar el diseño externo de la etiqueta.
 */
@Composable
private fun FilterLabel(
    textResId: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(textResId),
        modifier = modifier,
        color = colorResource(R.color.text_secondary_light),
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(5.dp))
}


/**
 * Muestra visualmente el valor seleccionado de calificación mínima.
 *
 * El control es estático por ahora y únicamente reproduce la apariencia
 * del deslizador mostrado en el diseño.
 */
@Composable
private fun RatingSlider(
    modifier: Modifier = Modifier
) {
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
            Text("0", fontSize = 7.sp)
            Text("1", fontSize = 7.sp)
            Text("2", fontSize = 7.sp)
            Text("3", fontSize = 7.sp)
            Text("4", fontSize = 7.sp)
            Text("5", fontSize = 7.sp)
        }
    }
}

/**
 * Muestra una vista previa del panel de filtros.
 */
@Composable
@Preview(showBackground = true)
fun SearchFilterPanelPreview() {
    SearchFilterPanel(
        modifier = Modifier.padding(20.dp)
    )
}

/** Muestra una vista previa de una etiqueta de filtro. */
@Composable
@Preview(showBackground = true)
fun FilterLabelPreview() {
    FilterLabel(textResId = R.string.brand, modifier = Modifier.padding(16.dp))
}

/** Muestra una vista previa del deslizador estático de calificación. */
@Composable
@Preview(showBackground = true)
fun RatingSliderPreview() {
    RatingSlider(modifier = Modifier.padding(16.dp))
}
