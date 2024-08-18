package com.wagaapp.foodguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wagaapp.foodguide.data.Dish
import com.wagaapp.foodguide.data.DishIngredient
import com.wagaapp.foodguide.data.Ingredient
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import parseCSV

class IngredientDishesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ingredientId = intent.getIntExtra("ingredientId", -1)
        val (dishes, ingredients, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val ingredient = ingredients.find { it.ingredientId == ingredientId }
        val ingredientDishList = dishIngredients.filter { it.ingredientId == ingredientId }
        setContent {
            FoodGuideTheme {
                IngredientDishesScreen(ingredient, ingredientDishList, dishes)
            }
        }
    }
}

@Composable
fun IngredientDishesScreen(ingredient: Ingredient?, ingredientDishes: List<DishIngredient>, dishes: List<Dish>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ingredient?.let {
            Text(text = it.ingredientName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(ingredientDishes) { ingredientDish ->
                    val dish = dishes.find { it.dishId == ingredientDish.dishId }
                    dish?.let {
                        Text(text = "${it.dishName}: ${ingredientDish.weight}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientDishesScreenPreview() {
    FoodGuideTheme {
        IngredientDishesScreen(
            ingredient = Ingredient(1, "Ingredient 1"),
            ingredientDishes = listOf(DishIngredient(1, 1, 100.0.toString())),
            dishes = listOf(Dish(1, "Dish 1", false))
        )
    }
}