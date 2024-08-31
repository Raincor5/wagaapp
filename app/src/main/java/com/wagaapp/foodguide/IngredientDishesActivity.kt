package com.wagaapp.foodguide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                IngredientDishesScreen(ingredient, ingredientDishList, dishes, ::navigateToDishIngredientsActivity, ::toggleIngredientUsedInAllDishes)
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

    private fun toggleIngredientUsedInAllDishes(ingredientId: Int) {
        val (_, _, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val ingredientDishList = dishIngredients.filter { it.ingredientId == ingredientId }
        ingredientDishList.forEach { it.isUsed = !it.isUsed }
        saveUsedIngredients(ingredientDishList)
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
    toggleIngredientUsedInAllDishes: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ingredient?.let {
                HeaderSection(title = "Dishes with ${it.ingredientName}")
            }
        }
        items(ingredientDishes) { ingredientDish ->
            val dish = dishes.find { it.dishId == ingredientDish.dishId }
            dish?.let {
                Text(
                    text = it.dishName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToDishIngredientsActivity(it.dishId) }
                        .padding(16.dp)
                )
            }
        }
    }
}