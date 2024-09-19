package com.wagaapp.foodguide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wagaapp.foodguide.composables.HeaderSection
import com.wagaapp.foodguide.composables.ItemComposable
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import com.wagaapp.foodguide.utils.parseCSV

class DishesActivity : ComponentActivity() {
    private val favoriteDishesKey = "favorite_dishes"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("com.wagaapp.foodguide", Context.MODE_PRIVATE)
        val favoriteDishes = getFavoriteDishes()
        val (dishes, _, _) = parseCSV(assets.open("dishes.csv"))
        setContent {
            FoodGuideTheme {
                DishesScreen(
                    dishes,
                    onDishClick = { dishId -> navigateToDishIngredientActivity(dishId) },
                    onFavoriteClick = { dishId -> toggleFavorite(dishId) },
                    favoriteDishes = favoriteDishes
                )
            }
        }
    }

    private fun getFavoriteDishes(): MutableSet<Int> {
        val favoriteDishesString = sharedPreferences.getStringSet(favoriteDishesKey, emptySet())
        return favoriteDishesString?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()
    }

    private fun saveFavoriteDishes(favoriteDishes: Set<Int>) {
        val favoriteDishesString = favoriteDishes.map { it.toString() }.toSet()
        sharedPreferences.edit().putStringSet(favoriteDishesKey, favoriteDishesString).apply()
    }

    private fun toggleFavorite(dishId: Int) {
        val favoriteDishes = getFavoriteDishes()
        if (favoriteDishes.contains(dishId)) {
            favoriteDishes.remove(dishId)
        } else {
            favoriteDishes.add(dishId)
        }
        saveFavoriteDishes(favoriteDishes)
    }

    private fun navigateToDishIngredientActivity(dishId: Int) {
        val intent = Intent(this, DishIngredientsActivity::class.java)
        intent.putExtra("dishId", dishId)
        startActivity(intent)
    }
}

@Composable
fun DishesScreen(dishes: List<Dish>, onDishClick: (Int) -> Unit, onFavoriteClick: (Int) -> Unit, favoriteDishes: Set<Int>) {
    var favoriteDishesState by remember { mutableStateOf(favoriteDishes) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section
        HeaderSection(title = "Dishes")

        // Dishes List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(dishes) { dish ->
                val isFavorite = favoriteDishesState.contains(dish.dishId)

                // Apply the ItemComposable with appropriate parameters
                ItemComposable(
                    text = dish.dishName,
                    onClick = { onDishClick(dish.dishId) },
                    onFavoriteClick = {
                        if (isFavorite) {
                            favoriteDishesState = favoriteDishesState - dish.dishId
                        } else {
                            favoriteDishesState = favoriteDishesState + dish.dishId
                        }
                        onFavoriteClick(dish.dishId)
                    },
                    isFavorite = isFavorite
                )
            }
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