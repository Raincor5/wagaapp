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

class DishIngredientsActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val usedIngredientsKey = "used_ingredients"
    private var allMarkedAsUsed = false
    private var previousState: List<DishIngredient> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("com.wagaapp.foodguide", Context.MODE_PRIVATE)
        val dishId = intent.getIntExtra("dishId", -1)
        val (dishes, ingredients, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val dish = dishes.find { it.dishId == dishId }
        val dishIngredientList = dishIngredients.filter { it.dishId == dishId }
        loadUsedIngredients(dishIngredientList)
        setContent {
            FoodGuideTheme {
                DishIngredientScreen(
                    dish = dish,
                    dishIngredients = dishIngredientList,
                    ingredients = ingredients,
                    navigateToIngredientDishesActivity = ::navigateToIngredientDishesActivity,
                    toggleIngredientUsed = { dishIngredient, isUsed -> toggleIngredientUsed(dishIngredient, isUsed) },
                    toggleAllIngredientsUsed = ::toggleAllIngredientsUsed,
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

    private fun toggleIngredientUsed(dishIngredient: DishIngredient, isUsed: Boolean) {
        dishIngredient.isUsed = isUsed
        saveUsedIngredients(listOf(dishIngredient))
    }

    private fun navigateToIngredientDishesActivity(ingredientId: Int) {
        val intent = Intent(this, IngredientDishesActivity::class.java)
        intent.putExtra("ingredientId", ingredientId)
        startActivity(intent)
    }

    private fun toggleAllIngredientsUsed(dishIngredients: List<DishIngredient>) {
        if (allMarkedAsUsed) {
            // Undo to previous state
            dishIngredients.forEachIndexed { index, dishIngredient ->
                dishIngredient.isUsed = previousState[index].isUsed
            }
        } else {
            // Save current state and mark all as used
            previousState = dishIngredients.map { it.copy() }
            dishIngredients.forEach { it.isUsed = true }
        }
        allMarkedAsUsed = !allMarkedAsUsed
        saveUsedIngredients(dishIngredients)
    }
}

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            HeaderSection(title = "Ingredients for ${it.dishName}")
        }
        Button(
            onClick = { toggleAllIngredientsUsed(dishIngredients) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (allMarkedAsUsed) "Undo" else "Mark All as Used")
        }
        LazyColumn {
            items(dishIngredients) { dishIngredient ->
                val ingredient = ingredients.find { it.ingredientId == dishIngredient.ingredientId }
                ingredient?.let {
                    ListItem(
                        name = it.ingredientName,
                        isUsed = dishIngredient.isUsed,
                        onNameClick = { navigateToIngredientDishesActivity(it.ingredientId) },
                        onSwitchClick = { isUsed -> toggleIngredientUsed(dishIngredient, isUsed) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DishIngredientScreenPreview() {
    val sampleDish = Dish(dishId = 1, dishName = "Sample Dish", isFavorite = false)
    val sampleIngredients = listOf(
        Ingredient(ingredientId = 1, ingredientName = "Ingredient 1"),
        Ingredient(ingredientId = 2, ingredientName = "Ingredient 2")
    )
    val sampleDishIngredients = listOf(
        DishIngredient(dishId = 1, ingredientId = 1, isUsed = true, weight = "100g"),
        DishIngredient(dishId = 1, ingredientId = 2, isUsed = false, weight = "200g")
    )

    FoodGuideTheme {
        DishIngredientScreen(
            dish = sampleDish,
            dishIngredients = sampleDishIngredients,
            ingredients = sampleIngredients,
            navigateToIngredientDishesActivity = {},
            toggleIngredientUsed = { _, _ -> },
            toggleAllIngredientsUsed = { _, -> },
            allMarkedAsUsed = false
        )
    }
}