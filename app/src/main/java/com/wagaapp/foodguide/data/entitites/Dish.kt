package com.wagaapp.foodguide.data.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dish(
    @PrimaryKey val dishId: Int,
    val dishName: String,
    val isFavorite: Boolean = false
)