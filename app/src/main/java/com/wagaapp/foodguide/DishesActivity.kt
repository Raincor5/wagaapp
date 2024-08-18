package com.wagaapp.foodguide

import ItemComposable
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
import com.wagaapp.foodguide.data.Dish
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import parseCSV

class DishesActivity : ComponentActivity() {
    private val favoriteDishes = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val (dishes, _, _) = parseCSV(assets.open("dishes.csv"))
        setContent {
            FoodGuideTheme {
                DishesScreen(dishes, { dishId -> navigateToDishIngredientActivity(dishId) }, { dishId -> toggleFavorite(dishId) }, favoriteDishes)
            }
        }
    }

    private fun toggleFavorite(dishId: Int) {
        if (favoriteDishes.contains(dishId)) {
            favoriteDishes.remove(dishId)
        } else {
            favoriteDishes.add(dishId)
        }
    }

    private fun navigateToDishIngredientActivity(dishId: Int) {
        val intent = Intent(this, DishIngredientsActivity::class.java)
        intent.putExtra("dishId", dishId)
        startActivity(intent)
    }
}

@Composable
fun DishesScreen(dishes: List<Dish>, onDishClick: (Int) -> Unit, onFavoriteClick: (Int) -> Unit, favoriteDishes: Set<Int>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(dishes) { dish ->
            ItemComposable(
                text = dish.dishName,
                onClick = { onDishClick(dish.dishId) },
                onFavoriteClick = { onFavoriteClick(dish.dishId) },
                isFavorite = favoriteDishes.contains(dish.dishId)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DishesScreenPreview() {
    FoodGuideTheme {
        DishesScreen(
            listOf(
                Dish(1, "Dish 1", false)
            ),
            onDishClick = {},
            onFavoriteClick = {},
            favoriteDishes = emptySet()
        )
    }
}