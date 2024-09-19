package com.wagaapp.foodguide.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef
import com.wagaapp.foodguide.data.entitites.Ingredient

data class DishWithIngredients(
    @Embedded val dish: Dish,
    @Relation(
        parentColumn = "dishId",
        entityColumn = "ingredientId",
        associateBy = Junction(DishIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)