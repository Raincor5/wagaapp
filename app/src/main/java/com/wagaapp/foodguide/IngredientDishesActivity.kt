package com.wagaapp.foodguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

class IngredientDishesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ingredientId = intent.getIntExtra("ingredientId", -1)
        val (dishes, ingredients, dishIngredients) = parseCSV(assets.open("dishes.csv"))
        val ingredient = ingredients.find { it.ingredientId == ingredientId }
        val ingredientDishList = dishIngredients.filter { it.ingredientId == ingredientId }
        setContent {
            FoodGuideTheme {
                IngredientDishesScreen(ingredient, ingredientDishList, dishes, ::navigateToDishIngredientsActivity)
            }
        }
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
    navigateToDishIngredientsActivity: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ingredient?.let {
            SectionHeader(title = "Dishes Containing ${ingredient.ingredientName}")
            Spacer(modifier = Modifier.height(16.dp))
            DishesList(ingredientDishes, dishes, navigateToDishIngredientsActivity)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "★",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "★",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun DishesList(
    ingredientDishes: List<DishIngredient>,
    dishes: List<Dish>,
    navigateToDishIngredientsActivity: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(ingredientDishes) { ingredientDish ->
            val dish = dishes.find { it.dishId == ingredientDish.dishId }
            dish?.let {
                DishItem(dishName = it.dishName, weight = ingredientDish.weight, onClick = { navigateToDishIngredientsActivity(it.dishId) })
            }
        }
    }
}

@Composable
fun DishItem(dishName: String, weight: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .border(1.dp, Color.Red, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = "★",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$dishName: $weight",
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientDishesScreenPreview() {
    FoodGuideTheme {
        IngredientDishesScreen(
            ingredient = Ingredient(1, "Ingredient 1"),
            ingredientDishes = listOf(DishIngredient(1, 1, "100g")),
            dishes = listOf(Dish(1, "Dish 1", false)),
            navigateToDishIngredientsActivity = {}
        )
    }
}