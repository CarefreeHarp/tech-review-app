package com.example.devicersapp.ui.screens.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.edit_profile.components.EditProfileField
import com.example.devicersapp.ui.screens.edit_profile.components.EditProfilePhoto
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileLoadStatus
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.navigation.TopBarStyle

/** Observa el formulario y conecta el selector de fotos con su ViewModel explícito. */
@Composable
fun EditProfileView(
    viewModel: EditProfileViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { photo ->
        photo?.let(viewModel::onPhotoSelected)
    }
    EditProfileViewContent(state = state, onUsernameChange = viewModel::onUsernameChange,
        onBiographyChange = viewModel::onBiographyChange, onChoosePhoto = { picker.launch("image/*") },
        onSave = { viewModel.save(onSaved) }, onCancel = onCancel, onRetry = viewModel::loadProfile,
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background))
}

/** Ensambla la foto y los campos del perfil sin decidir la identidad ni persistir datos. */
@Composable
fun EditProfileViewContent(
    state: EditProfileState,
    onUsernameChange: (String) -> Unit,
    onBiographyChange: (String) -> Unit,
    onChoosePhoto: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    if (state.loading) {
        Box(modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator(color = colors.primary) }
        return
    }
    if (!state.loaded) {
        ProfileLoadStatus(state.errorMessageResId, onRetry, modifier)
        return
    }
    LazyColumn(modifier = modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Column(Modifier.fillMaxWidth().padding(top = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                EditProfilePhoto(state.selectedPhoto?.toString() ?: state.profileImageUrl, onChoosePhoto)
            }
        }
        item { EditProfileField(R.string.username, state.username, onUsernameChange) }
        item { EditProfileField(R.string.edit_profile_biography, state.biography, onBiographyChange, multiline = true) }
        item { EditProfileField(R.string.email, state.email, {}, readOnly = true) }
        item {
            state.errorMessageResId?.let { Text(stringResource(it), color = colors.error, style = MaterialTheme.typography.bodyMedium) }
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.textOnPrimary)) {
                Text(stringResource(R.string.edit_review_save), style = MaterialTheme.typography.labelLarge)
            }
            TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                Text(stringResource(R.string.edit_profile_cancel), color = colors.primaryText)
            }
        }
    }
}

/** Previsualiza el formulario sin crear cuentas ni conectarse a servicios remotos. */
@Preview(showBackground = true, heightDp = 900)
@Composable
fun EditProfileViewPreview() {
    val username = stringResource(R.string.username_placeholder)
    val biography = stringResource(R.string.profile_biography)
    val email = stringResource(R.string.email_placeholder)
    DevicersAppTheme {
        DevicersScaffold(topBarStyle = TopBarStyle.EditProfile) { padding ->
            EditProfileViewContent(state = EditProfileState(username = username, biography = biography, email = email, loading = false, loaded = true),
                onUsernameChange = {}, onBiographyChange = {}, onChoosePhoto = {}, onSave = {},
                onCancel = {}, onRetry = {}, modifier = Modifier.padding(padding))
        }
    }
}
