// DishIngredientsActivity.kt
package com.wagaapp.foodguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.wagaapp.foodguide.data.AppDatabase
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.Ingredient
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef as DishIngredient
import com.wagaapp.foodguide.composables.DishIngredientScreen
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme

class DishIngredientsActivity : ComponentActivity() {

    private val viewModel: DishIngredientsViewModel by viewModels {
        DishIngredientsViewModelFactory(
            AppDatabase.getDatabase(this).dishDao(),
            AppDatabase.getDatabase(this).ingredientDao(),
            AppDatabase.getDatabase(this).dishIngredientCrossRefDao()
        )
    }

    private var allMarkedAsUsed: Boolean = false
    private var previousState: List<DishIngredient> = emptyList()

    private val sharedPreferences by lazy {
        getSharedPreferences("food_guide_prefs", MODE_PRIVATE)
    }
    private val usedIngredientsKey = "used_ingredients_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodGuideTheme {
                val dishId = intent.getIntExtra("dishId", -1)
                if (dishId != -1) {
                    viewModel.loadDish(dishId)
                    viewModel.loadDishIngredients(dishId)
                    viewModel.loadIngredients()
                }

                val dish by viewModel.dish.observeAsState()
                val dishIngredients by viewModel.dishIngredients.observeAsState(emptyList())
                val ingredients by viewModel.ingredients.observeAsState(emptyList())

                DishIngredientScreen(
                    dish = dish,
                    dishIngredients = dishIngredients,
                    ingredients = ingredients,
                    navigateToIngredientDishesActivity = ::navigateToIngredientDishesActivity,
                    toggleIngredientUsed = { dishIngredient, isUsed -> toggleIngredientUsed(dishIngredient, isUsed) },
                    toggleAllIngredientsUsed = { toggleAllIngredientsUsed(dishIngredients) },
                    allMarkedAsUsed = allMarkedAsUsed
                )
            }
        }
    }

    private fun navigateToIngredientDishesActivity(ingredientId: Int) {
        val intent = Intent(this, IngredientDishesActivity::class.java)
        intent.putExtra("ingredientId", ingredientId)
        startActivity(intent)
    }

    private fun toggleIngredientUsed(dishIngredient: DishIngredient, isUsed: Boolean) {
        dishIngredient.isUsed = isUsed
        saveUsedIngredients(listOf(dishIngredient))
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

    private fun saveUsedIngredients(dishIngredients: List<DishIngredient>) {
        val usedIngredients = dishIngredients.filter { it.isUsed }.map { "${it.dishId}_${it.ingredientId}" }.toSet()
        sharedPreferences.edit().putStringSet(usedIngredientsKey, usedIngredients).apply()
    }
}