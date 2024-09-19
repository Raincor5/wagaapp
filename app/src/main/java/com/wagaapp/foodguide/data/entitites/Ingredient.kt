package com.wagaapp.foodguide.data.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey val ingredientId: Int,
    val ingredientName: String,
    val isFavorite: Boolean = false
)