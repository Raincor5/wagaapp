package com.wagaapp.foodguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodGuideTheme {
                MainScreen(
                    onDishesClick = { navigateToDishesActivity() },
                    onIngredientsClick = { navigateToIngredientsActivity() }
                )
            }
        }
    }

    private fun navigateToDishesActivity() {
        val intent = Intent(this, DishesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToIngredientsActivity() {
        val intent = Intent(this, IngredientsActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun MainScreen(onDishesClick: () -> Unit, onIngredientsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        ImageButton(
            imageRes = R.drawable.image1,
            text = "Dishes",
            onClick = onDishesClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        ImageButton(
            imageRes = R.drawable.image2,
            text = "Ingredients",
            onClick = onIngredientsClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ImageButton(imageRes: Int, text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
                .blur(
                    radiusX = 8.dp,
                    radiusY = 8.dp,
                    edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(10.dp))
                ),
        )
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 24.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FoodGuideTheme {
        MainScreen(
            onDishesClick = {},
            onIngredientsClick = {}
        )
    }
}