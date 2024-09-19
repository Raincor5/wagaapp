package com.wagaapp.foodguide.data

import android.content.Context
import com.wagaapp.foodguide.data.entitites.SessionData
import com.wagaapp.foodguide.utils.parseCSV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseInitializer(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun initializeDatabase() {
        // Delete the existing database
        context.deleteDatabase("food_guide_database")

        // Recreate the database
        val db = AppDatabase.getDatabase(context)
        val dishDao = db.dishDao()
        val ingredientDao = db.ingredientDao()
        val dishIngredientCrossRefDao = db.dishIngredientCrossRefDao()
        val sessionDataDao = db.sessionDataDao()

        scope.launch {
            if (dishDao.getAllDishes().isEmpty()) {
                val (dishes, ingredients, dishIngredients) = parseCSV(context.assets.open("dishes.csv"))

                dishDao.insertAll(dishes)
                ingredientDao.insertAll(ingredients)
                dishIngredientCrossRefDao.insertAll(dishIngredients)
            }

            if (sessionDataDao.getAllSessionData().isEmpty()) {
                val initialSessionData = generateInitialSessionData()
                sessionDataDao.insertSessionData(initialSessionData)
            }
        }
    }

    private fun generateInitialSessionData(): List<SessionData> {
        return listOf(
            SessionData(sessionId = 1, ingredientId = 1, dishId = 1, isUsed = false, timestamp = Long.MIN_VALUE),
            SessionData(sessionId = 1, ingredientId = 2, dishId = 1, isUsed = false, timestamp = Long.MIN_VALUE)
            // Add more initial data as needed
        )
    }
}