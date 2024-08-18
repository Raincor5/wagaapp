package com.wagaapp.foodguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wagaapp.foodguide.data.Ingredient
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import parseCSV


class IngredientsActivity : ComponentActivity() {
    private val favoriteIngredients = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val (_, ingredients, _) = parseCSV(assets.open("dishes.csv"))
        setContent {
            FoodGuideTheme {
                IngredientsScreen(ingredients, { ingredientId -> navigateToIngredientDishesActivity(ingredientId) }, { ingredientId -> toggleFavorite(ingredientId) }, favoriteIngredients)
            }
        }
    }

    private fun toggleFavorite(ingredientId: Int) {
        if (favoriteIngredients.contains(ingredientId)) {
            favoriteIngredients.remove(ingredientId)
        } else {
            favoriteIngredients.add(ingredientId)
        }
    }

    private fun navigateToIngredientDishesActivity(ingredientId: Int) {
        val intent = Intent(this, IngredientDishesActivity::class.java)
        intent.putExtra("ingredientId", ingredientId)
        startActivity(intent)
    }
}

@Composable
fun IngredientsScreen(ingredients: List<Ingredient>, onIngredientClick: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(ingredients) { ingredient ->
            Text(
                text = ingredient.ingredientName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onIngredientClick(ingredient.ingredientId) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientsScreenPreview() {
    FoodGuideTheme {
        IngredientsScreen(
            listOf(
                Ingredient(1, "Ingredient 1")
            )
        ) {}
    }
}