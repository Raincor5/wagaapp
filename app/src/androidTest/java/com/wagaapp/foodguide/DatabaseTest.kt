package com.wagaapp.foodguide

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wagaapp.foodguide.data.AppDatabase
import com.wagaapp.foodguide.data.dao.DishDao
import com.wagaapp.foodguide.data.dao.IngredientDao
import com.wagaapp.foodguide.data.dao.DishIngredientCrossRefDao
import com.wagaapp.foodguide.data.dao.SessionDataDao
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.Ingredient
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef
import com.wagaapp.foodguide.utils.parseCSV
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var dishDao: DishDao
    private lateinit var ingredientDao: IngredientDao
    private lateinit var dishIngredientCrossRefDao: DishIngredientCrossRefDao
    private lateinit var sessionDataDao: SessionDataDao
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dishDao = db.dishDao()
        ingredientDao = db.ingredientDao()
        dishIngredientCrossRefDao = db.dishIngredientCrossRefDao()
        sessionDataDao = db.sessionDataDao()
    }

    @After
    fun closeDb() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun testParseCSV() = runBlocking {
        // Create a mock CSV file
        val csvContent = """
            dish_id,dish_name,ingredient_id,ingredient_name,weight
            1,Yaki Soba,1,Yaki Spring Onions,20g
            1,Yaki Soba,2,Red Peppers Teppan Cut,20g
        """.trimIndent()
        val csvFile = File(context.cacheDir, "mock_dishes.csv")
        csvFile.writeText(csvContent)

        // Parse the CSV file
        val inputStream = csvFile.inputStream()
        val (dishes, ingredients, dishIngredientCrossRefs) = parseCSV(inputStream)

        // Verify parsed data
        assertEquals(1, dishes.size)
        assertEquals(2, ingredients.size)
        assertEquals(2, dishIngredientCrossRefs.size)

        // Verify isFavorite is false
        assertEquals(false, dishes[0].isFavorite)
        assertEquals(false, ingredients[0].isFavorite)
    }

    @Test
    fun testDatabaseInsertion() = runBlocking {
        // Create mock data
        val dishes = listOf(Dish(1, "Yaki Soba"))
        val ingredients = listOf(Ingredient(1, "Yaki Spring Onions"), Ingredient(2, "Red Peppers Teppan Cut"))
        val dishIngredientCrossRefs = listOf(
            DishIngredientCrossRef(1, 1, "20g"),
            DishIngredientCrossRef(1, 2, "20g")
        )

        // Insert data into the database
        dishDao.insertAll(dishes)
        ingredientDao.insertAll(ingredients)
        dishIngredientCrossRefDao.insertAll(dishIngredientCrossRefs)

        // Query the database
        val dbDishes = dishDao.getAllDishes()
        val dbIngredients = ingredientDao.getAllIngredients()
        val dbCrossRefs = dishIngredientCrossRefDao.getIngredientsForDish(1)

        // Verify data
        assertEquals(dishes, dbDishes)
        assertEquals(ingredients, dbIngredients)
        assertEquals(dishIngredientCrossRefs, dbCrossRefs)

        // Verify isFavorite is false
        assertEquals(false, dbDishes[0].isFavorite)
        assertEquals(false, dbIngredients[0].isFavorite)
    }
}