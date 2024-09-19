package com.wagaapp.foodguide.data.dao

import androidx.room.*
import com.wagaapp.foodguide.data.DishWithIngredients
import com.wagaapp.foodguide.data.entitites.Dish

@Dao
interface DishDao {
    @Query("SELECT * FROM Dish")
    fun getAllDishes(): List<Dish>

    @Transaction
    @Query("SELECT * FROM Dish WHERE dishId = :dishId")
    fun getDishesWithIngredients(dishId: Int): List<DishWithIngredients>

    @Query("UPDATE Dish SET isFavorite = :isFavorite WHERE dishId = :dishId")
    fun markDishAsFavorite(dishId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dishes: List<Dish>)

    @Delete
    suspend fun delete(dish: Dish)

    @Query("SELECT * FROM Dish WHERE dishId = :dishId")
    suspend fun getDishById(dishId: Int): Dish?
}