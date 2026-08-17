package com.example.devicersapp.ui.utils.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.SearchControlText
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.clickable

/**
 * Muestra la barra de navegación inferior de la aplicación.
 *
 * @param selectedItem Identificador del elemento que representa la pantalla actual.
 * @param modifier Modificador aplicado a la barra.
 */
/**
 * Muestra la barra de navegación inferior de la aplicación.
 *
 * @param selectedItem Identificador del elemento que representa la pantalla actual.
 * @param modifier Modificador aplicado a la barra.
 * @param onProfileClick Acción ejecutada al presionar el ícono de perfil.
 */
@Composable
fun BottomNavigationBar(
    selectedItem: String,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavigationItem(
            iconResId = R.drawable.home_icon,
            isSelected = selectedItem == "home"
        )

        NavigationItem(
            iconResId = R.drawable.explore_icon,
            isSelected = selectedItem == "search"
        )

        NavigationItem(
            iconResId = R.drawable.create_review_icon,
            isSelected = selectedItem == "add"
        )

        NavigationItem(
            iconResId = R.drawable.notifications_icon,
            isSelected = selectedItem == "favorite"
        )

        NavigationItem(
            iconResId = R.drawable.profile_icon,
            isSelected = selectedItem == "profile",
            onClick = onProfileClick
        )
    }
}


/**
 * Muestra un elemento individual de la barra de navegación.
 *
 * @param iconResId Recurso del ícono.
 * @param isSelected Indica si el elemento está seleccionado.
 * @param modifier Modificador aplicado al elemento.
 * @param onClick Acción ejecutada al presionar el elemento.
 */
@Composable
fun NavigationItem(
    @DrawableRes iconResId: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

/**
 * Muestra una opción visual de filtro con su estado de selección.
 *
 * @param textResId Recurso de texto mostrado en la opción.
 * @param selected Indica si la opción debe aparecer seleccionada.
 * @param modifier Modificador aplicado a la opción.
 */
@Composable
fun FilterChip(
    @StringRes textResId: Int,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (selected) {
                    colorResource(R.color.primary_yellow)
                } else {
                    colorResource(R.color.surface_light)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textResId),
            color = colorResource(R.color.text_primary_light),
            style = SearchControlText,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Muestra una barra de búsqueda reutilizable con una lupa opcional.
 *
 * @param placeholder Recurso de texto mostrado cuando no hay búsqueda.
 * @param backgroundColor Recurso de color para el fondo del campo.
 * @param showSearchIcon Indica si debe mostrarse el ícono de lupa.
 * @param modifier Modificador aplicado al campo.
 */
@Composable
fun SearchBar(
    @StringRes placeholder: Int,
    backgroundColor: Int = R.color.background_light,
    showSearchIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(
                color = colorResource(backgroundColor),
                shape = RoundedCornerShape(9.dp)
            )
            .padding(horizontal = 12.dp),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = colorResource(R.color.text_primary_light),
            fontSize = SearchControlText.fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showSearchIcon) {
                    Image(
                        painter = painterResource(R.drawable.explore_icon),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(6.dp))
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(placeholder),
                            color = colorResource(R.color.text_secondary_light),
                            style = SearchControlText
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/**
 * Muestra la barra superior compartida de detalle, con regreso, ícono de marca y opciones.
 *
 * @param modifier Modificador aplicado al contenedor de la barra.
 */
@Composable
fun AppTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(R.string.top_bar_back),
            fontSize = 28.sp,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier.align(Alignment.CenterStart)
        )
        val logoRes = if (isSystemInDarkTheme()) {
            R.drawable.logo_icono_oscuro
        } else {
            R.drawable.logo_icono_claro
        }

        Image(
            painter = painterResource(logoRes),
            contentDescription = stringResource(R.string.devicers_logo_description),
            modifier = Modifier
                .height(44.dp)
                .align(Alignment.Center)
        )
        Text(
            text = stringResource(R.string.top_bar_options),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

/** Muestra una vista previa de la navegación inferior. */
@Composable
@Preview(showBackground = true)
fun BottomNavigationBarPreview() {
    BottomNavigationBar(selectedItem = "search")
}

/** Muestra una vista previa de un elemento seleccionado de navegación. */
@Composable
@Preview(showBackground = true)
fun NavigationItemPreview() {
    NavigationItem(R.drawable.home_icon, isSelected = true)
}

/** Muestra una vista previa de una opción de filtro seleccionada. */
@Composable
@Preview(showBackground = true)
fun FilterChipPreview() {
    FilterChip(R.string.all, selected = true)
}

/** Muestra una vista previa de la barra de búsqueda. */
@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    SearchBar(
        placeholder = R.string.search_placeholder,
        backgroundColor = R.color.surface_light,
        showSearchIcon = true
    )
}

/** Muestra una vista previa de la barra superior compartida. */
@Composable
@Preview(showBackground = true)
fun AppTopBarPreview() {
    AppTopBar()
}
