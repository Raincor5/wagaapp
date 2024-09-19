package com.wagaapp.foodguide.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef
import com.wagaapp.foodguide.data.entitites.Ingredient

data class IngredientWithDishes(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "dishId",
        associateBy = Junction(DishIngredientCrossRef::class)
    )
    val dishes: List<Dish>
)