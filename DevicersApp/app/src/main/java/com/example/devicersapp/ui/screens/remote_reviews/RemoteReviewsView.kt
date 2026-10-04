package com.example.devicersapp.ui.screens.remote_reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.repository.CURRENT_USER_ID
import com.example.devicersapp.ui.mappers.*
import com.example.devicersapp.ui.screens.rate_product.components.RatingSelector
import com.example.devicersapp.ui.screens.review.components.*
import com.example.devicersapp.ui.theme.LocalDevicersColors

@Composable
fun RemoteReviewCard(
    review: ReviewDto,
    onClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    Column(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        review.article?.let { ReviewProductSummary(it.toProductContent()) }
        ReviewDetail(review.toReviewContent())
        actions()
    }
}

@Composable
fun RemoteReviewsView(
    viewModel: RemoteReviewsViewModel,
    reviewId: Int? = null,
    onProfileClick: (String) -> Unit = {},
    onCreateClick: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(reviewId) { viewModel.load(reviewId) }
    LazyColumn(
        Modifier.fillMaxSize()
            .background(LocalDevicersColors.current.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 110.dp),
    ) {
        item {
            Text(
                if (reviewId == null) "Mis reseñas" else "Reseña",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        if (state.loading || state.saving) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        state.error?.let {
            item {
                Text(it, color = MaterialTheme.colorScheme.error)
                if (!state.editing)
                    TextButton(onClick = viewModel::retry, enabled = !state.saving) {
                        Text("Reintentar")
                    }
            }
        }
        if (state.editing) {
            item { Text("Editar reseña", style = MaterialTheme.typography.titleLarge) }
            item {
                RatingSelector(rating = state.rating, onRatingChange = viewModel::rating)
                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::title,
                    label = { Text("Título (opcional)") },
                    enabled = !state.saving,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = state.body,
                    onValueChange = viewModel::body,
                    label = { Text("Tu experiencia") },
                    minLines = 4,
                    enabled = !state.saving,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row {
                    Button(onClick = viewModel::save, enabled = !state.saving) { Text("Guardar") }
                    TextButton(onClick = viewModel::cancel, enabled = !state.saving) {
                        Text("Cancelar")
                    }
                }
            }
        } else {
            if (reviewId == null)
                item {
                    Button(onClick = onCreateClick, enabled = !state.loading && !state.saving) {
                        Text("Crear reseña")
                    }
                }
            if (!state.loading && state.reviews.isEmpty())
                item { Text("No hay reseñas para mostrar.") }
            items(state.reviews, key = { it.id }) { review ->
                RemoteReviewCard(review) {
                    TextButton(onClick = { onProfileClick(review.userId.toString()) }) {
                        Text("Ver perfil del autor")
                    }
                    if (review.userId == CURRENT_USER_ID)
                        Row {
                            TextButton(
                                onClick = { viewModel.edit(review) },
                                enabled = !state.saving,
                            ) {
                                Text("Editar")
                            }
                            TextButton(
                                onClick = { viewModel.requestDelete(review) },
                                enabled = !state.saving,
                            ) {
                                Text("Eliminar")
                            }
                        }
                }
            }
        }
    }
    if (state.deleteId != null)
        AlertDialog(
            onDismissRequest = viewModel::cancel,
            title = { Text("Eliminar reseña") },
            text = {
                Column {
                    Text("¿Quieres eliminar esta reseña?")
                    state.error?.let { Text(it) }
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::delete, enabled = !state.saving) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancel, enabled = !state.saving) {
                    Text("Cancelar")
                }
            },
        )
}
