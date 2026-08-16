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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer

/**
 * Muestra la barra de navegación inferior de la aplicación.
 *
 * @param selectedItem Identificador del elemento que representa la pantalla actual.
 * @param modifier Modificador aplicado a la barra.
 */
@Composable
fun BottomNavigationBar(selectedItem: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(colorResource(R.color.surface_light))
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(R.drawable.home_icon, selectedItem == "home")
        NavigationItem(R.drawable.explore_icon, selectedItem == "search")
        NavigationItem(R.drawable.create_review_icon, selectedItem == "add")
        NavigationItem(R.drawable.notifications_icon, selectedItem == "favorite")
        NavigationItem(R.drawable.profile_icon, selectedItem == "profile")
    }
}

/**
 * Muestra un elemento individual de la navegación inferior con su estado seleccionado.
 *
 * @param iconResId Recurso del ícono mostrado.
 * @param isSelected Indica si el elemento representa la pantalla actual.
 * @param modifier Modificador aplicado al elemento.
 */
@Composable
fun NavigationItem(
    @DrawableRes iconResId: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(
                color = if (isSelected) {
                    colorResource(R.color.primary_yellow)
                } else {
                    colorResource(R.color.surface_light)
                },
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (selected)
                    colorResource(R.color.primary_yellow)
                else
                    colorResource(R.color.surface_light),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
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
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit
)  {


    BasicTextField(
        value = text,
        onValueChange = onTextChange,
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
                    Spacer(modifier = Modifier.width(6.dp))
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
        Image(
            painter = painterResource(R.drawable.logo_icono_claro),
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
/*@Composable
@Preview(showBackground = true)
fun FilterChipPreview() {
    FilterChip(R.string.all, selected = true)
}
*/
/** Muestra una vista previa de la barra de búsqueda. */
/*@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    SearchBar(
        placeholder = R.string.search_placeholder,
        backgroundColor = R.color.surface_light,
        showSearchIcon = true
    )
}*/

/** Muestra una vista previa de la barra superior compartida. */
@Composable
@Preview(showBackground = true)
fun AppTopBarPreview() {
    AppTopBar()
}
