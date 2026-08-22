package com.example.devicersapp.ui.utils.navigation

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import androidx.compose.ui.text.font.FontWeight
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.authentication.AuthenticationHeader

/** Muestra la barra superior común de producto y perfil. */
@Composable
fun TopBar1(
    onBackClick: () -> Unit = {},
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
                contentDescription = stringResource(R.string.review_back),
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

/** Muestra la barra superior de calificación de producto. */
@Composable
fun TopBar2(
    onBackClick: () -> Unit = {},
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
                text = stringResource(R.string.rate_product_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary
            )
        }
        // El paso del flujo se acentúa para que el usuario sepa cuánto falta.
        Text(
            text = stringResource(R.string.rate_product_step),
            modifier = Modifier.padding(start = 12.dp),
            style = SearchControlText,
            fontWeight = FontWeight.Bold,
            color = colors.primaryText
        )
    }
}

/** Muestra la barra superior de creación de reseña. */
@Composable
fun TopBar3(
    onBackClick: () -> Unit = {},
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
                text = stringResource(R.string.create_review_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary
            )
        }
        // El paso del flujo se acentúa para que el usuario sepa cuánto falta.
        Text(
            text = stringResource(R.string.create_review_step),
            modifier = Modifier.padding(start = 12.dp),
            style = SearchControlText,
            fontWeight = FontWeight.Bold,
            color = colors.primaryText
        )
    }
}

/** Muestra la barra superior del detalle de una reseña. */
@Composable
fun TopBar4(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
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
}

/** Muestra la barra superior del detalle de producto, con regreso y acción de guardar. */
@Composable
fun TopBar6(
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
fun TopBar5(modifier: Modifier = Modifier) {
    AuthenticationHeader(
        modifier = modifier
            // Evita que el encabezado se dibuje bajo el notch o la barra de estado.
            .statusBarsPadding()
            // El relleno simétrico evita que la marca centrada se desplace hacia un costado.
            .padding(horizontal = 20.dp)
    )
}

/** Muestra una vista previa de la barra superior común. */
@Composable
@Preview(showBackground = true)
fun TopBar1Preview() { DevicersAppTheme { TopBar1() } }

/** Muestra una vista previa de la barra superior de calificación. */
@Composable
@Preview(showBackground = true)
fun TopBar2Preview() { DevicersAppTheme { TopBar2() } }

/** Muestra una vista previa de la barra superior de creación. */
@Composable
@Preview(showBackground = true)
fun TopBar3Preview() { DevicersAppTheme { TopBar3() } }

/** Muestra una vista previa de la barra superior de detalle. */
@Composable
@Preview(showBackground = true)
fun TopBar4Preview() { DevicersAppTheme { TopBar4() } }

/** Muestra una vista previa de la barra superior de autenticación. */
@Composable
@Preview(showBackground = true)
fun TopBar5Preview() { DevicersAppTheme { TopBar5() } }

/** Muestra una vista previa de la barra superior del detalle de producto. */
@Composable
@Preview(showBackground = true)
fun TopBar6Preview() { DevicersAppTheme { TopBar6() } }

/** Muestra una vista previa de la barra superior con título. */
@Composable
@Preview(showBackground = true)
fun TitleTopBarPreview() {
    DevicersAppTheme { TitleTopBar(R.string.profile_search_results_title) }
}
