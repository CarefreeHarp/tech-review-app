package com.example.devicersapp.ui.utils.navigation

import androidx.compose.foundation.clickable
import com.example.devicersapp.ui.utils.profile.asUserHandle

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import androidx.compose.ui.text.font.FontWeight
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.ScreenTitleText
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra la barra superior del perfil actualmente consultado.
 *
 * @param userHandleResId Recurso usado como alias de respaldo para perfiles locales.
 * @param userHandle Alias dinámico del perfil autenticado que tiene prioridad sobre el recurso local.
 * @param onBackClick Acción solicitada al regresar desde el perfil.
 * @param onOptionsClick Solicita abrir la barra lateral del perfil.
 * @param modifier Modificador aplicado al contenedor de la barra.
 * @param userId Identificador del usuario al que conduce el alias.
 */
@Composable
fun ProfileTopBar(
    @StringRes userHandleResId: Int? = null,
    userHandle: String? = null,
    onBackClick: () -> Unit = {},
    onOptionsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    userId: String? = null,
    onProfileClick: (String) -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    val optionsDescription = stringResource(R.string.profile_options)
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
                .statusBarsPadding()
                .height(58.dp)
                .padding(horizontal = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.review_back),
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            val visibleUserHandle = userHandle?.takeIf { it.isNotBlank() }
                ?: userHandleResId?.let { stringResource(it) }
            if (visibleUserHandle != null) {
                Text(
                    text = visibleUserHandle.asUserHandle(),
                    modifier = Modifier.align(Alignment.Center).then(if (!userId.isNullOrBlank()) Modifier.clickable { onProfileClick(userId) } else Modifier),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(onClick = onOptionsClick,
                    modifier = Modifier.semantics { contentDescription = optionsDescription }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(colors.textPrimary, CircleShape)
                            )
                            if (index < 2) {
                                Spacer(modifier = Modifier.size(3.dp))
                            }
                        }
                    }
                }

            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            thickness = 3.dp,
            color = colors.border
        )
    }
}

/** Muestra la barra superior de calificación de producto. */
@Composable
fun RateProductTopBar(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ReviewStepTopBar(
        titleResId = R.string.rate_product_title,
        stepResId = R.string.rate_product_step,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

/** Muestra la barra superior de creación de reseña. */
@Composable
fun CreateReviewTopBar(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ReviewStepTopBar(
        titleResId = R.string.create_review_title,
        stepResId = R.string.create_review_step,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

/**
 * Comparte la composición del encabezado de los dos pasos de publicación de reseñas.
 *
 * @param titleResId Título del paso actual.
 * @param stepResId Indicación del paso que se muestra debajo del título.
 * @param onBackClick Acción solicitada al regresar.
 * @param modifier Modificador aplicado al contenedor del encabezado.
 */
@Composable
private fun ReviewStepTopBar(
    @StringRes titleResId: Int,
    @StringRes stepResId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
            .statusBarsPadding()
            .padding(horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.top_bar_back),
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.size(6.dp))
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = stringResource(stepResId),
            modifier = Modifier.padding(start = 12.dp),
            style = SearchControlText,
            fontWeight = FontWeight.Bold,
            color = colors.primaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )
    }
}

/** Muestra la barra superior del detalle de una reseña. */
@Composable
fun ReviewTopBar(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Column() {
        Row(
            modifier = modifier
                .fillMaxWidth()
                // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
                .statusBarsPadding()
                .height(58.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.top_bar_back),
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.size(6.dp))
            Text(
                text = stringResource(R.string.review_title),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary
            )
            IconButton(onClick = onSaveClick) {
                Icon(
                    painter = painterResource(R.drawable.bookmark_icon),
                    contentDescription = stringResource(R.string.review_save),
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            thickness = 3.dp,
            color = colors.border
        )
    }
}

/** Muestra la barra superior del detalle de producto, con regreso y acción de guardar. */
@Composable
fun ProductTopBar(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
            .statusBarsPadding()
            .height(48.dp)
            .padding(horizontal = 12.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.top_bar_back),
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = onSaveClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.bookmark_icon),
                contentDescription = stringResource(R.string.product_save),
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Muestra una barra superior de regreso con el título que reciba.
 *
 * Las pantallas cuyo encabezado solo cambia en su texto comparten esta barra en lugar de
 * declarar una variante propia por cada una.
 *
 * @param titleResId Recurso de texto mostrado como título.
 * @param onBackClick Acción solicitada al volver.
 * @param modifier Modificador aplicado a la barra.
 */
@Composable
fun TitleTopBar(
    @StringRes titleResId: Int,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
            .statusBarsPadding()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.top_bar_back),
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.size(6.dp))
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary
        )
    }
}

/** Muestra la barra superior de marca compartida por inicio de sesión y registro. */
@Composable
fun AuthenticationTopBar(modifier: Modifier = Modifier) {
    AuthenticationHeader(
        modifier = modifier
            // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
            .statusBarsPadding()
            // El relleno simétrico evita que la marca centrada se desplace hacia un costado.
            .padding(horizontal = 20.dp)
    )
}

/** Muestra la barra superior de la actividad reciente y el avatar del usuario autenticado. */
@Composable
fun ActivityTopBar(
    profileImageUrl: String? = null,
    profileId: String? = null,
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                // Mantiene el título por debajo de la barra de estado en dispositivos con notch.
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.activity_title),
                    style = ScreenTitleText,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.activity_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
            }
            ProfileAvatar(
                onProfileClick = onProfileClick,
                avatarResId = R.drawable.no_pfp_icon,
                imageUrl = profileImageUrl,
                profileId = profileId,
                modifier = Modifier.size(42.dp)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            thickness = 3.dp,
            color = colors.border
        )
    }
}

/** Muestra una vista previa de la barra superior común. */
@Composable
@Preview(showBackground = true)
fun ProfileTopBarPreview() { DevicersAppTheme { ProfileTopBar(userHandleResId = R.string.profile_handle) } }

/** Muestra una vista previa de la barra superior de calificación. */
@Composable
@Preview(showBackground = true)
fun RateProductTopBarPreview() { DevicersAppTheme { RateProductTopBar() } }

/** Muestra una vista previa de la barra superior de creación. */
@Composable
@Preview(showBackground = true)
fun CreateReviewTopBarPreview() { DevicersAppTheme { CreateReviewTopBar() } }

/** Muestra una vista previa de la barra superior de detalle. */
@Composable
@Preview(showBackground = true)
fun ReviewTopBarPreview() { DevicersAppTheme { ReviewTopBar() } }

/** Muestra una vista previa de la barra superior de autenticación. */
@Composable
@Preview(showBackground = true)
fun AuthenticationTopBarPreview() { DevicersAppTheme { AuthenticationTopBar() } }

/** Muestra una vista previa de la barra superior de actividad. */
@Composable
@Preview(showBackground = true)
fun ActivityTopBarPreview() { DevicersAppTheme { ActivityTopBar() } }

/** Muestra una vista previa de la barra superior del detalle de producto. */
@Composable
@Preview(showBackground = true)
fun ProductTopBarPreview() { DevicersAppTheme { ProductTopBar() } }

/** Muestra una vista previa de la barra superior con título. */
@Composable
@Preview(showBackground = true)
fun TitleTopBarPreview() {
    DevicersAppTheme { TitleTopBar(R.string.profile_search_results_title) }
}

/** Muestra una vista previa del encabezado compartido de los pasos de publicación. */
@Preview(showBackground = true)
@Composable
private fun ReviewStepTopBarPreview() {
    DevicersAppTheme {
        ReviewStepTopBar(
            titleResId = R.string.create_review_title,
            stepResId = R.string.create_review_step,
            onBackClick = {}
        )
    }
}
