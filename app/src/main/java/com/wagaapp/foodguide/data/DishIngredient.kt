package com.wagaapp.foodguide.data

data class DishIngredient(
    val dishId: Int,
    val ingredientId: Int,
    val weight: String,
    var isUsed: Boolean = false
)