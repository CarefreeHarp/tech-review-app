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
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.FilterChip
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Muestra el panel visual con los filtros disponibles para la búsqueda.
 *
 * @param modifier Modificador aplicado al panel.
 */
@Composable
fun SearchFilterPanel(
    brand: String,
    onBrandChange: (String) -> Unit,

    productName: String,
    onProductNameChange: (String) -> Unit,

    launchDate: String,
    onLaunchDateChange: (String) -> Unit,

    selectedCategory: String,
    onCategorySelected: (String) -> Unit,

    minimumRating: Float,
    onRatingChange: (Float) -> Unit,

    sortBy: String,
    onSortChange: (String) -> Unit,

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
                style = SearchHeadingText
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.clear_filters),
                color = colorResource(R.color.text_secondary_light),
                style = SearchControlText
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        FilterLabel(R.string.brand)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.brand_placeholder,
            backgroundColor = R.color.background_light,
            showSearchIcon = false,
            text = brand,
            onTextChange = onBrandChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.product_name)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.product_name_placeholder,
            backgroundColor = R.color.background_light,
            showSearchIcon = false,
            text = productName,
            onTextChange = onProductNameChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.launch_date)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.launch_date_placeholder,
            backgroundColor = R.color.background_light,
            showSearchIcon = false,
            text = launchDate,
            onTextChange = onLaunchDateChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.category)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                R.string.all,
                selected = selectedCategory == "all",
                onClick = { onCategorySelected("all") }
            )

            FilterChip(
                R.string.cellphones,
                selected = selectedCategory == "cellphones",
                onClick = { onCategorySelected("cellphones") }
            )

            FilterChip(
                R.string.audio,
                selected = selectedCategory == "audio",
                onClick = { onCategorySelected("audio") }
            )

            FilterChip(
                R.string.computers,
                selected = selectedCategory == "computers",
                onClick = { onCategorySelected("computers") }
            )
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
                style = SearchControlText
            )
        }
        RatingSlider(
            value = minimumRating,
            onValueChange = onRatingChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.sort_by)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                R.string.most_recent,
                selected = sortBy == "recent",
                onClick = { onSortChange("recent") },
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                R.string.best_rated,
                selected = sortBy == "rated",
                onClick = { onSortChange("rated") },
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
                style = SearchControlText
            )
        }
    }
}

/** Muestra una vista previa del panel de filtros. */
@Composable
@Preview(showBackground = true)
fun SearchFilterPanelPreview() {
    SearchFilterPanel(
        brand = "",
        onBrandChange = {},

        productName = "",
        onProductNameChange = {},

        launchDate = "",
        onLaunchDateChange = {},

        selectedCategory = "all",
        onCategorySelected = {},

        minimumRating = 4f,
        onRatingChange = {},

        sortBy = "recent",
        onSortChange = {},

        modifier = Modifier.padding(20.dp)
    )
}