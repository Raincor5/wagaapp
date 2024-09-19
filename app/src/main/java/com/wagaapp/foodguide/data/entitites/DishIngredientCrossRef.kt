package com.wagaapp.foodguide.data.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["dishId", "ingredientId"],
    indices = [Index(value = ["ingredientId"])]
)
data class DishIngredientCrossRef(
    @ColumnInfo(defaultValue = "0") val dishId: Int,
    val ingredientId: Int,
    val weight: String,
    var isUsed: Boolean = false
)