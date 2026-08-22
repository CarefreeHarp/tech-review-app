package com.example.devicersapp.ui.screens.search_profile.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.FilterChip
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.search.FilterLabel
import com.example.devicersapp.ui.utils.search.FilterSlider

/**
 * Muestra el panel con los filtros disponibles para buscar perfiles.
 *
 * @param username Nombre de usuario escrito para filtrar.
 * @param onUsernameChange Acción que solicita actualizar el nombre de usuario.
 * @param interests Intereses escritos para filtrar.
 * @param onInterestsChange Acción que solicita actualizar los intereses.
 * @param minimumReviews Cantidad mínima de reseñas publicadas.
 * @param onMinimumReviewsChange Acción que solicita cambiar la cantidad mínima de reseñas.
 * @param relationship Identificador de la relación activa con los perfiles buscados.
 * @param onRelationshipChange Acción que solicita cambiar la relación.
 * @param sortBy Identificador del orden activo.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer todos los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param modifier Modificador aplicado al panel.
 */
@Composable
fun SearchProfileFilterPanel(
    username: String,
    onUsernameChange: (String) -> Unit,

    interests: String,
    onInterestsChange: (String) -> Unit,

    minimumReviews: Float,
    onMinimumReviewsChange: (Float) -> Unit,

    relationship: String,
    onRelationshipChange: (String) -> Unit,

    sortBy: String,
    onSortChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp))
            // La superficie secundaria agrupa los filtros y los separa del fondo de la pantalla.
            .background(colors.surfaceSecondary, RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.search_filters),
                color = colors.textPrimary,
                style = SearchHeadingText
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.clear_filters),
                modifier = Modifier.clickable(onClick = onClearFilters),
                color = colors.primaryText,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.username)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.username_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            height = 42.dp,
            text = username,
            onTextChange = onUsernameChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.search_profile_interests)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.search_profile_interests_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = false,
            height = 42.dp,
            text = interests,
            onTextChange = onInterestsChange
        )

        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterLabel(R.string.search_profile_minimum_reviews, Modifier.weight(1f))
            Text(
                text = stringResource(R.string.search_profile_minimum_reviews_format, minimumReviews),
                color = colors.textPrimary,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }
        FilterSlider(
            value = minimumReviews,
            onValueChange = onMinimumReviewsChange,
            valueRange = 0f..50f,
            // Los saltos de cinco reseñas evitan valores difíciles de fijar con el dedo.
            steps = 9,
            trackColor = colors.primary
        )

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.search_profile_relationship)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                R.string.search_profile_all_users,
                selected = relationship == "all",
                onClick = { onRelationshipChange("all") }
            )
            FilterChip(
                R.string.search_profile_following,
                selected = relationship == "following",
                onClick = { onRelationshipChange("following") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.sort_by)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                R.string.search_profile_alphabetical,
                selected = sortBy == "alphabetical",
                onClick = { onSortChange("alphabetical") }
            )
            FilterChip(
                R.string.search_profile_most_reviews,
                selected = sortBy == "reviews",
                onClick = { onSortChange("reviews") }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onApplyFilters)
                .height(46.dp)
                .background(colors.primary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                color = colors.textOnPrimary,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Muestra una vista previa del panel de filtros de perfiles. */
@Composable
@Preview(showBackground = true, heightDp = 640)
fun SearchProfileFilterPanelPreview() {
    DevicersAppTheme {
        SearchProfileFilterPanel(
            username = "",
            onUsernameChange = {},

            interests = "",
            onInterestsChange = {},

            minimumReviews = 20f,
            onMinimumReviewsChange = {},

            relationship = "all",
            onRelationshipChange = {},

            sortBy = "alphabetical",
            onSortChange = {},
            onClearFilters = {},
            onApplyFilters = {},

            modifier = Modifier.padding(20.dp)
        )
    }
}
