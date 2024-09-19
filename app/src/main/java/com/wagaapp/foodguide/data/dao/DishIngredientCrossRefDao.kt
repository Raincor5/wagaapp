package com.wagaapp.foodguide.data.dao

import androidx.room.*
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef

@Dao
interface DishIngredientCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDishIngredientCrossRef(crossRef: DishIngredientCrossRef)

    @Transaction
    @Query("SELECT * FROM DishIngredientCrossRef WHERE dishId = :dishId")
    fun getIngredientsForDish(dishId: Int): List<DishIngredientCrossRef>

    @Transaction
    @Query("SELECT * FROM DishIngredientCrossRef WHERE ingredientId = :ingredientId")
    fun getDishesForIngredient(ingredientId: Int): List<DishIngredientCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dishIngredientCrossRefs: List<DishIngredientCrossRef>)

    @Delete
    suspend fun delete(crossRef: DishIngredientCrossRef)
}