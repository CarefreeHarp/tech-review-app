package com.example.devicersapp.ui.utils.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.TopBarActionText
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
        val logoRes = if (colors.isDarkTheme) R.drawable.logo_icono_oscuro else R.drawable.logo_icono_claro
        androidx.compose.foundation.Image(
            painter = painterResource(logoRes),
            contentDescription = stringResource(R.string.devicers_logo_description),
            modifier = Modifier
                .height(44.dp)
                .align(Alignment.Center)
        )
        Text(
            text = stringResource(R.string.top_bar_options),
            style = TopBarActionText,
            color = colors.textPrimary,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

/** Muestra la barra superior de calificación de producto. */
@Composable
fun TopBar2(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.rate_product_back),
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = stringResource(R.string.rate_product_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = stringResource(R.string.rate_product_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
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
        Text(
            text = stringResource(R.string.create_review_title),
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/** Muestra la barra superior del detalle de una reseña. */
@Composable
fun TopBar4(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.review_back),
                tint = LocalDevicersColors.current.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.size(18.dp))
        Text(
            text = stringResource(R.string.review_title),
            style = MaterialTheme.typography.titleLarge,
            color = LocalDevicersColors.current.textPrimary
        )
    }
}

/** Muestra la barra superior de marca compartida por inicio de sesión y registro. */
@Composable
fun TopBar5(modifier: Modifier = Modifier) {
    AuthenticationHeader(
        modifier = modifier.padding(start = 20.dp, top = 28.dp)
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
