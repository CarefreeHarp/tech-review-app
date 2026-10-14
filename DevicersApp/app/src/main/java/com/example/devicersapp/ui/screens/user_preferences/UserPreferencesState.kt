package com.example.devicersapp.ui.screens.user_preferences

import androidx.annotation.StringRes
import com.example.devicersapp.R

/** Define la presentación inicial de preferencias, sin identidad ni opciones configurables. */
data class UserPreferencesState(
    @param:StringRes val titleResId: Int = R.string.user_preferences_title,
    @param:StringRes val emptyMessageResId: Int = R.string.user_preferences_empty
)
