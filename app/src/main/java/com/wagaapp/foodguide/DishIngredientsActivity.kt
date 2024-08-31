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

class DishIngredientsActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val usedIngredientsKey = "used_ingredients"

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
                    toggleIngredientUsed = ::toggleIngredientUsed
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

    private fun toggleIngredientUsed(dishIngredient: DishIngredient) {
        dishIngredient.isUsed = !dishIngredient.isUsed
        saveUsedIngredients(listOf(dishIngredient))
    }

    private fun navigateToIngredientDishesActivity(ingredientId: Int) {
        val intent = Intent(this, IngredientDishesActivity::class.java)
        intent.putExtra("ingredientId", ingredientId)
        startActivity(intent)
    }
}

@Composable
fun DishIngredientScreen(
    dish: Dish?,
    dishIngredients: List<DishIngredient>,
    ingredients: List<Ingredient>,
    navigateToIngredientDishesActivity: (Int) -> Unit,
    toggleIngredientUsed: (DishIngredient) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            dish?.let {
                HeaderSection(title = "Ingredients for ${it.dishName}")
            }
        }
        items(dishIngredients) { dishIngredient ->
            val ingredient = ingredients.find { it.ingredientId == dishIngredient.ingredientId }
            ingredient?.let {
                Text(
                    text = "${it.ingredientName} - ${dishIngredient.weight}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToIngredientDishesActivity(it.ingredientId) }
                        .padding(16.dp)
                )
            }
        }
    }
}