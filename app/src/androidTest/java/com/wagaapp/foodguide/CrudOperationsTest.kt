package com.wagaapp.foodguide

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wagaapp.foodguide.data.AppDatabase
import com.wagaapp.foodguide.data.dao.*
import com.wagaapp.foodguide.data.entitites.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CrudOperationsTest {
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
    fun testDishCrudOperations() = runBlocking {
        // Create
        val dish = Dish(1, "Yaki Soba")
        dishDao.insertAll(listOf(dish))
        var dbDish = dishDao.getAllDishes().firstOrNull()
        assertNotNull(dbDish)
        assertEquals(dish, dbDish)

        // Read
        dbDish = dishDao.getDishesWithIngredients(1).firstOrNull()?.dish
        assertNotNull(dbDish)
        assertEquals(dish, dbDish)

        // Update
        dishDao.markDishAsFavorite(1, true)
        dbDish = dishDao.getAllDishes().firstOrNull()
        assertNotNull(dbDish)
        assertTrue(dbDish!!.isFavorite)

        // Delete
        dishDao.delete(dish)
        dbDish = dishDao.getAllDishes().firstOrNull()
        assertNull(dbDish)
    }

    @Test
    fun testIngredientCrudOperations() = runBlocking {
        // Create
        val ingredient = Ingredient(1, "Yaki Spring Onions")
        ingredientDao.insertAll(listOf(ingredient))
        var dbIngredient = ingredientDao.getAllIngredients().firstOrNull()
        assertNotNull(dbIngredient)
        assertEquals(ingredient, dbIngredient)

        // Read
        dbIngredient = ingredientDao.getIngredientsWithDishes(1).firstOrNull()?.ingredient
        assertNotNull(dbIngredient)
        assertEquals(ingredient, dbIngredient)

        // Update
        ingredientDao.markIngredientAsFavorite(1, true)
        dbIngredient = ingredientDao.getAllIngredients().firstOrNull()
        assertNotNull(dbIngredient)
        assertTrue(dbIngredient!!.isFavorite)

        // Delete
        ingredientDao.delete(ingredient)
        dbIngredient = ingredientDao.getAllIngredients().firstOrNull()
        assertNull(dbIngredient)
    }

    @Test
    fun testDishIngredientCrossRefCrudOperations() = runBlocking {
        // Create
        val crossRef = DishIngredientCrossRef(1, 1, "20g")
        dishIngredientCrossRefDao.insertDishIngredientCrossRef(crossRef)
        var dbCrossRef = dishIngredientCrossRefDao.getIngredientsForDish(1).firstOrNull()
        assertNotNull(dbCrossRef)
        assertEquals(crossRef, dbCrossRef)

        // Read
        dbCrossRef = dishIngredientCrossRefDao.getDishesForIngredient(1).firstOrNull()
        assertNotNull(dbCrossRef)
        assertEquals(crossRef, dbCrossRef)

        // Update
        crossRef.isUsed = true
        dishIngredientCrossRefDao.insertDishIngredientCrossRef(crossRef)
        dbCrossRef = dishIngredientCrossRefDao.getIngredientsForDish(1).firstOrNull()
        assertNotNull(dbCrossRef)
        assertTrue(dbCrossRef!!.isUsed)

        // Delete
        dishIngredientCrossRefDao.delete(crossRef)
        dbCrossRef = dishIngredientCrossRefDao.getIngredientsForDish(1).firstOrNull()
        assertNull(dbCrossRef)
    }

    @Test
    fun testSessionDataCrudOperations() = runBlocking {
        // Create
        val sessionData = SessionData(1, 1, 1, 1, false, System.currentTimeMillis())
        sessionDataDao.insertSessionData(listOf(sessionData))
        var rowsDeleted = sessionDataDao.clearOldSessionData(System.currentTimeMillis() - 1000)
        assertEquals(0, rowsDeleted)

        // Read
        rowsDeleted = sessionDataDao.clearOldSessionData(System.currentTimeMillis() + 1000)
        assertEquals(1, rowsDeleted)
    }
}