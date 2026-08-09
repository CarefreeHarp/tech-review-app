package com.example.devicersapp.ui.utils.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

/**
 * Muestra la barra de navegación inferior de la aplicación.
 *
 * El elemento correspondiente a la pantalla actual recibe un fondo amarillo.
 * La barra es únicamente visual y todavía no implementa navegación.
 *
 * @param selectedItem Identificador del elemento que representa la pantalla actual.
 * @param modifier Permite modificar el diseño externo de la barra.
 */
@Composable
fun BottomNavigationBar(
    selectedItem: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(color = colorResource(R.color.surface_light))
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavigationItem(
            iconResId = R.drawable.home,
            isSelected = selectedItem == "home"
        )

        NavigationItem(
            iconResId = R.drawable.search,
            isSelected = selectedItem == "search"
        )

        NavigationItem(
            iconResId = R.drawable.add,
            isSelected = selectedItem == "add"
        )

        NavigationItem(
            iconResId = R.drawable.like,
            isSelected = selectedItem == "favorite"
        )

        NavigationItem(
            iconResId = R.drawable.profile,
            isSelected = selectedItem == "profile"
        )
    }
}

/**
 * Muestra un elemento individual de la barra de navegación.
 *
 * Cuando el elemento está seleccionado, se coloca un fondo amarillo
 * detrás del PNG para indicar visualmente la pantalla actual.
 *
 * @param iconResId Recurso PNG que representa el elemento.
 * @param isSelected Indica si el elemento corresponde a la pantalla actual.
 * @param modifier Permite modificar el diseño externo del elemento.
 */
@Composable
private fun NavigationItem(
    iconResId: Int,
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
 * Muestra una vista previa de la barra con la opción de inicio seleccionada.
 */
@Composable
@Preview(showBackground = true)
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        selectedItem = "search"
    )
}

/** Muestra una vista previa de un elemento seleccionado de la navegación inferior. */
@Composable
@Preview(showBackground = true)
fun NavigationItemPreview() {
    NavigationItem(iconResId = R.drawable.home, isSelected = true)
}
