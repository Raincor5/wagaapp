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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wagaapp.foodguide.data.Dish
import com.wagaapp.foodguide.data.DishIngredient
import com.wagaapp.foodguide.data.Ingredient
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import com.wagaapp.foodguide.composables.*
import parseCSV

class IngredientDishesActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val usedIngredientsKey = "used_ingredients"
    private var allMarkedAsUsed = false
    private var previousState: List<DishIngredient> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("com.wagaapp.foodguide", Context.MODE_PRIVATE)
        val ingredientId = intent.getIntExtra("ingredientId", -1)
        val (dishes, ingredients, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val ingredient = ingredients.find { it.ingredientId == ingredientId }
        val ingredientDishList = dishIngredients.filter { it.ingredientId == ingredientId }
        loadUsedIngredients(ingredientDishList)
        setContent {
            FoodGuideTheme {
                IngredientDishesScreen(
                    ingredient = ingredient,
                    ingredientDishes = ingredientDishList,
                    dishes = dishes,
                    navigateToDishIngredientsActivity = ::navigateToDishIngredientsActivity,
                    toggleIngredientUsedInAllDishes = ::toggleIngredientUsedInAllDishes,
                    allMarkedAsUsed = allMarkedAsUsed
                )
            }
        }
    }

    private fun loadUsedIngredients(dishIngredients: List<DishIngredient>) {
        val usedIngredients = sharedPreferences.getStringSet(usedIngredientsKey, emptySet()) ?: emptySet()
        dishIngredients.forEach { it.isUsed = usedIngredients.contains("${it.dishId}_${it.ingredientId}") }
    }

    private fun saveUsedIngredients(dishIngredients: List<DishIngredient>) {
        val usedIngredients = dishIngredients.filter { it.isUsed }.map { "${it.dishId}_${it.ingredientId}" }.toSet()
        sharedPreferences.edit().putStringSet(usedIngredientsKey, usedIngredients).apply()
    }

    private fun toggleIngredientUsedInAllDishes(ingredientDishes: List<DishIngredient>) {
        if (allMarkedAsUsed) {
            // Undo to previous state
            ingredientDishes.forEachIndexed { index, dishIngredient ->
                dishIngredient.isUsed = previousState[index].isUsed
            }
        } else {
            // Save current state and mark all as used
            previousState = ingredientDishes.map { it.copy() }
            ingredientDishes.forEach { it.isUsed = true }
        }
        allMarkedAsUsed = !allMarkedAsUsed
        saveUsedIngredients(ingredientDishes)
    }

    private fun navigateToDishIngredientsActivity(dishId: Int) {
        val intent = Intent(this, DishIngredientsActivity::class.java)
        intent.putExtra("dishId", dishId)
        startActivity(intent)
    }
}

@Composable
fun IngredientDishesScreen(
    ingredient: Ingredient?,
    ingredientDishes: List<DishIngredient>,
    dishes: List<Dish>,
    navigateToDishIngredientsActivity: (Int) -> Unit,
    toggleIngredientUsedInAllDishes: (List<DishIngredient>) -> Unit,
    allMarkedAsUsed: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ingredient?.let {
            HeaderSection(title = "Dishes with ${it.ingredientName}")
        }
        Button(
            onClick = { toggleIngredientUsedInAllDishes(ingredientDishes) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (allMarkedAsUsed) "Undo" else "Mark All as Used")
        }
        LazyColumn {
            items(ingredientDishes) { ingredientDish ->
                val dish = dishes.find { it.dishId == ingredientDish.dishId }
                dish?.let {
                    ListItem(
                        name = "${it.dishName} (${ingredientDish.weight})",
                        isUsed = ingredientDish.isUsed,
                        onNameClick = { navigateToDishIngredientsActivity(it.dishId) },
                        onSwitchClick = { isUsed -> toggleIngredientUsedInAllDishes(listOf(ingredientDish.copy(isUsed = isUsed))) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IngredientDishesScreenPreview() {
    val sampleIngredient = Ingredient(ingredientId = 1, ingredientName = "Sample Ingredient")
    val sampleDishes = listOf(
        Dish(dishId = 1, dishName = "Dish 1", isFavorite = false),
        Dish(dishId = 2, dishName = "Dish 2", isFavorite = false)
    )
    val sampleIngredientDishes = listOf(
        DishIngredient(dishId = 1, ingredientId = 1, isUsed = true, weight = "100g"),
        DishIngredient(dishId = 2, ingredientId = 1, isUsed = false, weight = "200g")
    )

    FoodGuideTheme {
        IngredientDishesScreen(
            ingredient = sampleIngredient,
            ingredientDishes = sampleIngredientDishes,
            dishes = sampleDishes,
            navigateToDishIngredientsActivity = {},
            toggleIngredientUsedInAllDishes = { _ -> },
            allMarkedAsUsed = false
        )
    }
}