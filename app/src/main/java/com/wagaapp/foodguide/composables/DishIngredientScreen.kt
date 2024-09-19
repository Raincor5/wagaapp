package com.wagaapp.foodguide.composables

import  androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wagaapp.foodguide.DishIngredientsViewModel
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef as DishIngredient
import com.wagaapp.foodguide.data.entitites.Ingredient
import com.wagaapp.foodguide.data.entitites.Dish

@Composable
fun DishIngredientScreen(
    dish: Dish?,
    dishIngredients: List<DishIngredient>,
    ingredients: List<Ingredient>,
    navigateToIngredientDishesActivity: (Int) -> Unit,
    toggleIngredientUsed: (DishIngredient, Boolean) -> Unit,
    toggleAllIngredientsUsed: (List<DishIngredient>) -> Unit,
    allMarkedAsUsed: Boolean
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            Text("Ingredients for ${it.dishName}")
        }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Ingredients") },
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = { toggleAllIngredientsUsed(dishIngredients) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (allMarkedAsUsed) "Undo" else "Mark All as Used")
        }
        LazyColumn {
            items(dishIngredients.filter { ingredient ->
                ingredients.find { it.ingredientId == ingredient.ingredientId }?.ingredientName?.contains(searchQuery, ignoreCase = true) == true
            }) { dishIngredient ->
                val ingredient = ingredients.find { it.ingredientId == dishIngredient.ingredientId }
                ingredient?.let {
                    ListItem(
                        name = it.ingredientName,
                        isUsed = dishIngredient.isUsed,
                        onNameClick = { navigateToIngredientDishesActivity(it.ingredientId) },
                        onSwitchClick = { isUsed: Boolean -> toggleIngredientUsed(dishIngredient, isUsed) }
                    )
                }
            }
        }
    }
}
