package com.wagaapp.foodguide.data.dao

import androidx.room.*
import com.wagaapp.foodguide.data.IngredientWithDishes
import com.wagaapp.foodguide.data.entitites.Ingredient

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getAllIngredients(): List<Ingredient>

    @Transaction
    @Query("SELECT * FROM Ingredient WHERE ingredientId = :ingredientId")
    fun getIngredientsWithDishes(ingredientId: Int): List<IngredientWithDishes>

    @Query("UPDATE Ingredient SET isFavorite = :isFavorite WHERE ingredientId = :ingredientId")
    fun markIngredientAsFavorite(ingredientId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredients: List<Ingredient>)

    @Delete
    suspend fun delete(ingredient: Ingredient)
}