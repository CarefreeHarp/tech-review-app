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
        NavigationItem(R.drawable.home, selectedItem == "home")
        NavigationItem(R.drawable.search, selectedItem == "search")
        NavigationItem(R.drawable.add, selectedItem == "add")
        NavigationItem(R.drawable.like, selectedItem == "favorite")
        NavigationItem(R.drawable.profile, selectedItem == "profile")
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
            modifier = Modifier.size(20.dp)
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
            .padding(horizontal = 10.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textResId),
            color = colorResource(R.color.text_primary_light),
            fontSize = 8.sp,
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
            .height(34.dp)
            .background(
                color = colorResource(backgroundColor),
                shape = RoundedCornerShape(9.dp)
            )
            .padding(horizontal = 12.dp),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = colorResource(R.color.text_primary_light),
            fontSize = 9.sp
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showSearchIcon) {
                    Image(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
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
                            fontSize = 9.sp
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
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
    NavigationItem(R.drawable.home, isSelected = true)
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
