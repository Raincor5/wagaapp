// TODO: Refactor the rest of the code to move the frequently used composable functions to this file
package com.wagaapp.foodguide.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeaderSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .padding(8.dp)
    )
}

// DishesActivity.kt and IngredientDishesActivity.kt
@Composable
fun ItemComposable(
    text: String,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Padding between items
            .border(1.dp, Color.Red, RoundedCornerShape(8.dp)) // Apply border first
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                ),
                shape = RoundedCornerShape(8.dp) // Ensure background respects rounded corners
            )
            .clickable { onClick() }
            .padding(16.dp) // Inner padding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Ensure text is black for readability
                )
            )
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isFavorite) Color(0xffff0000) else Color.Gray,
                modifier = Modifier.clickable { onFavoriteClick() }
            )
        }
    }
}


// DishIngredientsActivity.kt and IngredientDishesActivity.kt
@Composable
fun ListItem(
    name: String,
    isUsed: Boolean,
    onNameClick: () -> Unit,
    onSwitchClick: (Boolean) -> Unit
) {
    var switchState by remember { mutableStateOf(isUsed) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onNameClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Switch(
            checked = switchState,
            onCheckedChange = {
                switchState = it
                onSwitchClick(it)
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}
