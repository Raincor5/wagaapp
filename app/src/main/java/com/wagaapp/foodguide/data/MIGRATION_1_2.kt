package com.wagaapp.foodguide.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Check if the 'dishId' column exists before adding it
        val cursor = database.query("PRAGMA table_info(SessionData)")
        var columnExists = false
        while (cursor.moveToNext()) {
            val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            if (columnName == "dishId") {
                columnExists = true
                break
            }
        }
        cursor.close()

        if (!columnExists) {
            database.execSQL("ALTER TABLE SessionData ADD COLUMN dishId INTEGER NOT NULL DEFAULT 0")
        }
    }
}