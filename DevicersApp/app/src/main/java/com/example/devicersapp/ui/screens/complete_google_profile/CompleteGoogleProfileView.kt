package com.example.devicersapp.ui.screens.complete_google_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.access.components.AccessAuthenticationError
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.authentication.AuthenticationField
import com.example.devicersapp.ui.utils.authentication.PrimaryButton
import com.example.devicersapp.ui.utils.authentication.ScreenTitle

@Composable
fun CompleteGoogleProfileView(
    viewModel: CompleteGoogleProfileViewModel,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(36.dp))
        ScreenTitle(R.string.complete_profile_title, R.string.complete_profile_description)
        Spacer(Modifier.height(32.dp))
        AuthenticationField(
            labelResId = R.string.username,
            placeholderResId = R.string.username_placeholder,
            value = state.username,
            onValueChange = viewModel::onUsernameChange
        )
        Spacer(Modifier.height(10.dp))
        state.errorMessageResId?.let {
            AccessAuthenticationError(errorMessage = stringResource(it))
            Spacer(Modifier.height(18.dp))
        }
        PrimaryButton(
            textResId = R.string.complete_profile_continue,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            onClick = { if (!state.saving) viewModel.save(onCompleted) }
        )
    }
}
