package com.wagaapp.foodguide.data

import android.content.Context
import androidx.room.Room

fun getAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "foodguide-database"
    ).build()
}