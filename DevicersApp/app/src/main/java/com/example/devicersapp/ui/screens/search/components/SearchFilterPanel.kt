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
import com.example.devicersapp.ui.utils.navigation.FilterChip
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Muestra el panel visual con los filtros disponibles para la búsqueda.
 *
 * @param modifier Modificador aplicado al panel.
 */
@Composable
fun SearchFilterPanel(modifier: Modifier = Modifier) {
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
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(R.string.brand_placeholder, showSearchIcon = false, backgroundColor = R.color.background_light)

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.product_name)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(R.string.product_name_placeholder, showSearchIcon = false, backgroundColor = R.color.background_light)

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.launch_date)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(R.string.launch_date_placeholder, showSearchIcon = false, backgroundColor = R.color.background_light)

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.category)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(R.string.all, selected = true)
            FilterChip(R.string.cellphones, selected = false)
            FilterChip(R.string.audio, selected = false)
            FilterChip(R.string.computers, selected = false)
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterLabel(R.string.minimum_rating, Modifier.weight(1f))
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
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(R.string.most_recent, selected = true, modifier = Modifier.weight(1f))
            FilterChip(R.string.best_rated, selected = false, modifier = Modifier.weight(1f))
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

/** Muestra una vista previa del panel de filtros. */
@Composable
@Preview(showBackground = true)
fun SearchFilterPanelPreview() {
    SearchFilterPanel(modifier = Modifier.padding(20.dp))
}
