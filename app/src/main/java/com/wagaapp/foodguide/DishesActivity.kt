package com.wagaapp.foodguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme

class DishesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodGuideTheme {
                DishesScreen()
            }
        }
    }
}

@Composable
fun DishesScreen() {
    Text(text = "Welcome to Dishes Activity", style = MaterialTheme.typography.headlineMedium)
}

@Preview(showBackground = true)
@Composable
fun DishesScreenPreview() {
    FoodGuideTheme {
        DishesScreen()
    }
}