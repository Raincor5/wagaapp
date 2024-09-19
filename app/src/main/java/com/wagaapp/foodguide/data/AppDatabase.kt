package com.wagaapp.foodguide.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wagaapp.foodguide.data.dao.DishDao
import com.wagaapp.foodguide.data.dao.DishIngredientCrossRefDao
import com.wagaapp.foodguide.data.dao.IngredientDao
import com.wagaapp.foodguide.data.dao.SessionDataDao
import com.wagaapp.foodguide.data.entitites.Dish
import com.wagaapp.foodguide.data.entitites.DishIngredientCrossRef
import com.wagaapp.foodguide.data.entitites.Ingredient
import com.wagaapp.foodguide.data.entitites.SessionData

@Database(
    entities = [Dish::class, Ingredient::class, DishIngredientCrossRef::class, SessionData::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun dishIngredientCrossRefDao(): DishIngredientCrossRefDao
    abstract fun sessionDataDao(): SessionDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_guide_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}