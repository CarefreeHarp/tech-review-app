package com.example.devicersapp.ui.utils.review

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra la fila de interacciones de una reseña: reacción, comentarios, compartir y guardar.
 *
 * Las tres primeras acciones se agrupan a la izquierda y la de guardar se separa al extremo
 * derecho, tal como aparece en el feed y en el detalle de una reseña.
 *
 * @param likes Cantidad de reacciones recibidas.
 * @param comments Cantidad de comentarios recibidos.
 * @param modifier Modificador aplicado a la fila.
 * @param onLikeClick Acción solicitada al reaccionar a la reseña.
 * @param onCommentClick Acción solicitada al comentar la reseña.
 * @param onSendClick Acción solicitada al compartir la reseña.
 * @param onSaveClick Acción solicitada al guardar la reseña.
 */
@Composable
fun ReviewActionsRow(
    likes: Int,
    comments: Int,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReviewAction(
            iconResId = R.drawable.like_icon,
            contentDescriptionResId = R.string.review_action_like,
            count = likes,
            onClick = onLikeClick
        )
        Spacer(modifier = Modifier.width(22.dp))
        ReviewAction(
            iconResId = R.drawable.comment_icon,
            contentDescriptionResId = R.string.review_action_comment,
            count = comments,
            onClick = onCommentClick
        )
        Spacer(modifier = Modifier.width(22.dp))
        ReviewAction(
            iconResId = R.drawable.send_icon,
            contentDescriptionResId = R.string.review_action_send,
            onClick = onSendClick
        )
        Spacer(modifier = Modifier.weight(1f))
        ReviewAction(
            iconResId = R.drawable.bookmark_icon,
            contentDescriptionResId = R.string.review_action_save,
            onClick = onSaveClick
        )
    }
}

/**
 * Muestra una acción de la reseña con su ícono y, cuando corresponde, su conteo.
 *
 * @param iconResId Recurso del ícono de la acción.
 * @param contentDescriptionResId Recurso de texto usado como descripción accesible.
 * @param modifier Modificador aplicado a la acción.
 * @param count Cantidad acumulada de la acción, o `null` cuando la acción no acumula.
 * @param onClick Acción solicitada al pulsar el ícono.
 */
@Composable
fun ReviewAction(
    @DrawableRes iconResId: Int,
    @StringRes contentDescriptionResId: Int,
    modifier: Modifier = Modifier,
    count: Int? = null,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    Row(
        modifier = modifier.clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = stringResource(contentDescriptionResId),
            modifier = Modifier.size(19.dp),
            tint = colors.textSecondary
        )
        if (count != null) {
            // Los conteos van en negrita porque son el dato que el lector compara entre reseñas.
            Text(
                text = count.toString(),
                color = colors.textPrimary,
                style = CardHighlightText
            )
        }
    }
}

/** Muestra una vista previa de la fila completa de interacciones. */
@Composable
@Preview(showBackground = true)
fun ReviewActionsRowPreview() {
    DevicersAppTheme {
        ReviewActionsRow(likes = 128, comments = 18)
    }
}

/** Muestra una vista previa de una acción de reseña con conteo. */
@Composable
@Preview(showBackground = true)
fun ReviewActionPreview() {
    DevicersAppTheme {
        ReviewAction(
            iconResId = R.drawable.like_icon,
            contentDescriptionResId = R.string.review_action_like,
            count = 128
        )
    }
}
