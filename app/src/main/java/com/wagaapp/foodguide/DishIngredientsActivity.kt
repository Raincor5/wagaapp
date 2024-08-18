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

class DishIngredientsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dishId = intent.getIntExtra("dishId", -1)
        val (dishes, ingredients, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val dish = dishes.find { it.dishId == dishId }
        val dishIngredientList = dishIngredients.filter { it.dishId == dishId }
        setContent {
            FoodGuideTheme {
                DishIngredientScreen(dish, dishIngredientList, ingredients)
            }
        }
    }
}

@Composable
fun DishIngredientScreen(dish: Dish?, dishIngredients: List<DishIngredient>, ingredients: List<Ingredient>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            Text(text = it.dishName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(dishIngredients) { dishIngredient ->
                    val ingredient = ingredients.find { it.ingredientId == dishIngredient.ingredientId }
                    ingredient?.let {
                        Text(text = "${it.ingredientName}: ${dishIngredient.weight}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description and cooking instructions will be added here.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DishIngredientScreenPreview() {
    FoodGuideTheme {
        DishIngredientScreen(
            dish = Dish(1, "Dish 1", false),
            dishIngredients = listOf(DishIngredient(1, 1, 100.0.toString())),
            ingredients = listOf(Ingredient(1, "Ingredient 1"))
        )
    }
}