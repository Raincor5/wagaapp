package com.wagaapp.foodguide.data.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val ingredientId: Int,
    @ColumnInfo(defaultValue = "0") val dishId: Int,
    val isUsed: Boolean,
    val timestamp: Long
)