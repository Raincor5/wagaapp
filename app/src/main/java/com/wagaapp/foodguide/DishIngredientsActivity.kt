package com.wagaapp.foodguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                DishIngredientScreen(dish, dishIngredientList, ingredients, ::navigateToIngredientDishesActivity)
            }
        }
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
    navigateToIngredientDishesActivity: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            HeaderSection(title = it.dishName)
            Spacer(modifier = Modifier.height(16.dp))
            IngredientListSection(dishIngredients, ingredients, navigateToIngredientDishesActivity)
            Spacer(modifier = Modifier.height(16.dp))
            InstructionsSection()
            Spacer(modifier = Modifier.height(16.dp))
            FooterSection()
        }
    }
}

@Composable
fun IngredientListSection(
    dishIngredients: List<DishIngredient>,
    ingredients: List<Ingredient>,
    navigateToIngredientDishesActivity: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.Red, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        dishIngredients.forEach { dishIngredient ->
            val ingredient = ingredients.find { it.ingredientId == dishIngredient.ingredientId }
            ingredient?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToIngredientDishesActivity(it.ingredientId) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = it.ingredientName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = dishIngredient.weight,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderSection(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp)) // Apply border first
            .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp)) // Ensure background respects rounded corners)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@Composable
fun InstructionsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Overall padding for the section
    ) {
        val instructions = listOf(
            "Step 1: Do this.",
            "Step 2: Do that.",
            "Step 3: Finish up."
        )
        instructions.forEachIndexed { index, instruction ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp) // Space between items
                    .background(
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(1.dp, Color.Red, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp) // Inner padding for text content
            ) {
                Text(
                    text = "${index + 1}. $instruction",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun FooterSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Additional notes or links.",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DishIngredientScreenPreview() {
    FoodGuideTheme {
        DishIngredientScreen(
            dish = Dish(1, "Dish 1", false),
            dishIngredients = listOf(DishIngredient(1, 1, 100.0.toString())),
            ingredients = listOf(Ingredient(1, "Ingredient 1")),
            navigateToIngredientDishesActivity = {}
        )
    }
}