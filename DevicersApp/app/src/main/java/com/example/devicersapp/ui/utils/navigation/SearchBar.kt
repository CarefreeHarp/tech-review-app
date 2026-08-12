package com.example.devicersapp.ui.utils.navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource
/**
 * Muestra las posibles barras de búsqueda que se usarán en el programa.
 *
 * El componente es únicamente visual y todavía no permite introducir
 * texto ni ejecutar búsquedas.
 *
 * @param placeholder permite cambiar el nombre que aparece en la barra
 * @param backgroundColor permite cambiar el color de fondo de la barra según donde se use
 * @param showSearchIcon permite colocar el ícono de lupa si es necesari
 * @param modifier Permite modificar el diseño externo de la barra.
 */
@Composable
fun SearchBar(
    placeholder: Int,
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
                            fontSize = 9.sp
                        )
                    }

                    innerTextField()
                }
            }
        }
    )
}
/**
 * Muestra una vista previa de la barra de búsqueda

 **/
@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    SearchBar(
        placeholder = R.string.search_placeholder,
        showSearchIcon = true,
        backgroundColor = R.color.surface_light
    )
}
