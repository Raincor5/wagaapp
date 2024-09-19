package com.wagaapp.foodguide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wagaapp.foodguide.composables.HeaderSection
import com.wagaapp.foodguide.composables.ItemComposable
import com.wagaapp.foodguide.data.entitites.Ingredient
import com.wagaapp.foodguide.ui.theme.FoodGuideTheme
import com.wagaapp.foodguide.utils.parseCSV

class IngredientsActivity : ComponentActivity() {
    private val favoriteIngredientsKey = "favorite_ingredients"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("com.wagaapp.foodguide", Context.MODE_PRIVATE)
        val favoriteIngredients = getFavoriteIngredients()
        val (_, ingredients, _) = parseCSV(assets.open("dishes.csv"))
        setContent {
            FoodGuideTheme {
                IngredientsScreen(
                    ingredients,
                    onIngredientClick = { ingredientId -> navigateToIngredientDishesActivity(ingredientId) },
                    onFavoriteClick = { ingredientId -> toggleFavorite(ingredientId) },
                    favoriteIngredients = favoriteIngredients
                )
            }
        }
    }

    private fun getFavoriteIngredients(): MutableSet<Int> {
        val favoriteIngredientsString = sharedPreferences.getStringSet(favoriteIngredientsKey, emptySet())
        return favoriteIngredientsString?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()
    }

    private fun saveFavoriteIngredients(favoriteIngredients: Set<Int>) {
        val favoriteIngredientsString = favoriteIngredients.map { it.toString() }.toSet()
        sharedPreferences.edit().putStringSet(favoriteIngredientsKey, favoriteIngredientsString).apply()
    }

    private fun toggleFavorite(ingredientId: Int) {
        val favoriteIngredients = getFavoriteIngredients()
        if (favoriteIngredients.contains(ingredientId)) {
            favoriteIngredients.remove(ingredientId)
        } else {
            favoriteIngredients.add(ingredientId)
        }
        saveFavoriteIngredients(favoriteIngredients)
    }

    private fun navigateToIngredientDishesActivity(ingredientId: Int) {
        val intent = Intent(this, IngredientDishesActivity::class.java)
        intent.putExtra("ingredientId", ingredientId)
        startActivity(intent)
    }
}

@Composable
fun IngredientsScreen(
    ingredients: List<Ingredient>,
    onIngredientClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    favoriteIngredients: Set<Int>
) {
    var favoriteIngredientsState by remember { mutableStateOf(favoriteIngredients) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section
        HeaderSection(title = "Ingredient List")
        // Ingredients List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(ingredients) { index, ingredient ->
                val isFavorite = favoriteIngredientsState.contains(ingredient.ingredientId)


                ItemComposable(
                    text = ingredient.ingredientName,
                    onClick = { onIngredientClick(ingredient.ingredientId) },
                    onFavoriteClick = {
                        if (isFavorite) {
                            favoriteIngredientsState = favoriteIngredientsState - ingredient.ingredientId
                        } else {
                            favoriteIngredientsState = favoriteIngredientsState + ingredient.ingredientId
                        }
                        onFavoriteClick(ingredient.ingredientId)
                    },
                    isFavorite = isFavorite
                )

            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun IngredientsScreenPreview() {
    FoodGuideTheme {
        IngredientsScreen(
            listOf(
                Ingredient(1, "Ingredient 1")
            ),
            onIngredientClick = {},
            onFavoriteClick = {},
            favoriteIngredients = emptySet()
        )
    }
}