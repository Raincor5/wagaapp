package com.wagaapp.foodguide.utils

//import java.io.InputStream
//import java.io.InputStreamReader
//import com.opencsv.CSVReader
//import com.wagaapp.foodguide.data.entitites.Dish
//import com.wagaapp.foodguide.data.DishIngredient
//import com.wagaapp.foodguide.data.entitites.Ingredient

//fun parseCSV(inputStream: InputStream): Triple<List<Dish>, List<Ingredient>, List<DishIngredient>> {
//    val reader = CSVReader(InputStreamReader(inputStream))
//    val dishes = mutableListOf<Dish>()
//    val ingredients = mutableMapOf<Int, Ingredient>()
//    val dishIngredients = mutableListOf<DishIngredient>()
//
//    reader.readAll().drop(1).forEach { line ->
//        val dishId = line[0].toInt()
//        val dishName = line[1]
//        val ingredientId = line[2].toInt()
//        val ingredientName = line[3]
//        val weight = line[4]
//        val isFavorite = line[5].toBoolean()
//
//        if (dishes.none { it.dishId == dishId }) {
//            dishes.add(Dish(dishId, dishName, isFavorite))
//        }
//
//        if (!ingredients.containsKey(ingredientId)) {
//            ingredients[ingredientId] = Ingredient(ingredientId, ingredientName)
//        }
//
//        dishIngredients.add(DishIngredient(dishId, ingredientId, weight))
//    }
//
//    return Triple(dishes, ingredients.values.toList(), dishIngredients)
//}

import android.content.Context
import com.opencsv.CSVReader
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef
import com.wagaapp.foodguide.data.entitites.Ingredient
import java.io.InputStream
import java.io.InputStreamReader

fun parseCSV(inputStream: InputStream): Triple<List<Dish>, List<Ingredient>, List<DishIngredientCrossRef>> {
    val dishes = mutableListOf<Dish>()
    val ingredients = mutableListOf<Ingredient>()
    val dishIngredientCrossRefs = mutableListOf<DishIngredientCrossRef>()

    inputStream.use { stream ->
        val reader = CSVReader(InputStreamReader(stream))
        reader.readNext() // Skip header
        reader.forEach { line ->
            val dishId = line[0].toInt()
            val dishName = line[1]
            val ingredientId = line[2].toInt()
            val ingredientName = line[3]
            val weight = line[4]


            dishes.add(Dish(dishId, dishName))
            ingredients.add(Ingredient(ingredientId, ingredientName))
            dishIngredientCrossRefs.add(DishIngredientCrossRef(dishId, ingredientId, weight))
        }
    }

    return Triple(dishes.distinct(), ingredients.distinct(), dishIngredientCrossRefs)
}