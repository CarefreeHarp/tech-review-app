package com.example.devicersapp.ui.screens.edit_review

import com.example.devicersapp.ui.screens.edit_review.components.EditReviewSkeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.screens.edit_review.components.EditReviewStatus
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.review_form.RateableProductCard
import com.example.devicersapp.ui.utils.review_form.RatingSelector
import com.example.devicersapp.ui.utils.review_form.ReviewForm
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Observa el formulario de edición y comunica el guardado exitoso a la navegación. */
@Composable
fun EditReviewView(
    reviewId: Int,
    viewModel: EditReviewViewModel,
    onSaved: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(reviewId) { viewModel.loadReview(reviewId) }
    LaunchedEffect(state.saved) { if (state.saved) onSaved() }
    EditReviewViewContent(
        state = state,
        onRetry = { viewModel.loadReview(reviewId) },
        onRatingChange = viewModel::onRatingChange,
        onTitleChange = viewModel::onTitleChange,
        onExperienceChange = viewModel::onExperienceChange,
        onAdvantageChange = viewModel::onAdvantageChange,
        onDisadvantageChange = viewModel::onDisadvantageChange,
        onSave = viewModel::save,
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background)
    )
}

/** Ensambla la misma tarjeta y formulario de calificación, adaptados a guardar cambios. */
@Composable
fun EditReviewViewContent(
    state: EditReviewState,
    onRetry: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onAdvantageChange: (String) -> Unit,
    onDisadvantageChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    // Mantiene los datos escritos durante el envío y bloquea las acciones del formulario.
    if (state.loading) { EditReviewSkeleton(modifier); return }
    if (state.product == null || !state.canEdit) {
        EditReviewStatus(state = state, onRetry = onRetry, modifier = modifier)
        return
    }
    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
            Spacer(Modifier.height(20.dp))
            state.errorResId?.let { Text(stringResource(it), color = colors.textPrimary) }
            Text(stringResource(R.string.edit_review_description),
                style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
            Spacer(Modifier.height(20.dp))
            RateableProductCard(product = state.product, onChangeProduct = {})
            Spacer(Modifier.height(24.dp))
        }
        item {
            RatingSelector(rating = state.rating, onRatingChange = onRatingChange,
                enabled = !state.saving && !state.saved)
            Spacer(Modifier.height(28.dp))
        }
        item {
            ReviewForm(
                title = state.title, onTitleChange = onTitleChange,
                experience = state.experience, onExperienceChange = onExperienceChange,
                advantage = state.advantage, onAdvantageChange = onAdvantageChange,
                disadvantage = state.disadvantage, onDisadvantageChange = onDisadvantageChange,
                onPublishClick = onSave, enabled = !state.saving && !state.saved,
                submitLabelResId = R.string.edit_review_save,
                experienceMaxLength = null
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

/** Muestra el formulario de edición con datos precargados para revisar su presentación. */
@Preview(showBackground = true, heightDp = 1100)
@Composable
fun EditReviewViewPreview() {
    DevicersAppTheme {
        DevicersScaffold(topBarNumber = 11) { padding ->
            EditReviewViewContent(
                state = EditReviewState(product = LocalProductProvider.product, canEdit = true,
                    rating = 4, title = stringResource(R.string.edit_review_title),
                    experience = stringResource(R.string.edit_review_description)),
                onRetry = {}, onRatingChange = {}, onTitleChange = {},
                onExperienceChange = {}, onAdvantageChange = {}, onDisadvantageChange = {},
                onSave = {}, modifier = Modifier.padding(padding).fillMaxSize()
            )
        }
    }
}
