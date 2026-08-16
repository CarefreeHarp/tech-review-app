package com.example.devicersapp.ui.screens.rateProduct.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

// Muestra el formulario utilizado para escribir la calificación del producto.
@Composable
fun ReviewForm(
    title: String,
    onTitleChange: (String) -> Unit,
    experience: String,
    onExperienceChange: (String) -> Unit,
    advantage: String,
    onAdvantageChange: (String) -> Unit,
    disadvantage: String,
    onDisadvantageChange: (String) -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.rate_product_review_title),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary_light)
        )

        Spacer(modifier = Modifier.height(6.dp))

        ReviewInputField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = stringResource(R.string.rate_product_review_title_placeholder)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.rate_product_experience),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary_light)
        )

        Spacer(modifier = Modifier.height(6.dp))

        ReviewInputField(
            value = experience,
            onValueChange = onExperienceChange,
            placeholder = stringResource(R.string.rate_product_experience_placeholder),
            singleLine = false,
            minLines = 4,
            maxLength = 500
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = stringResource(R.string.rate_product_advantages),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(R.color.text_primary_light)
                )

                Spacer(modifier = Modifier.height(6.dp))

                ReviewInputField(
                    value = advantage,
                    onValueChange = onAdvantageChange,
                    placeholder = stringResource(R.string.rate_product_advantage_placeholder)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = stringResource(R.string.rate_product_disadvantages),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(R.color.text_primary_light)
                )

                Spacer(modifier = Modifier.height(6.dp))

                ReviewInputField(
                    value = disadvantage,
                    onValueChange = onDisadvantageChange,
                    placeholder = stringResource(R.string.rate_product_disadvantage_placeholder)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.rate_product_publish),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(R.color.rating_selected_light),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onPublishClick()
                }
                .padding(vertical = 14.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewFormPreview() {
    ReviewForm(
        title = "",
        onTitleChange = {},
        experience = "",
        onExperienceChange = {},
        advantage = "",
        onAdvantageChange = {},
        disadvantage = "",
        onDisadvantageChange = {},
        onPublishClick = {}
    )
}