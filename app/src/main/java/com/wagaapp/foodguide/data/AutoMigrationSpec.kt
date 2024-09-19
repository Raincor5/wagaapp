package com.wagaapp.foodguide.data

import androidx.room.migration.AutoMigrationSpec
import androidx.room.DeleteColumn
import androidx.room.RenameColumn

@DeleteColumn(
    tableName = "Dish",
    columnName = "id"
)
@RenameColumn(
    tableName = "Dish",
    fromColumnName = "id",
    toColumnName = "newId"
)
@RenameColumn(
    tableName = "Dish",
    fromColumnName = "name",
    toColumnName = "newName"
)
@RenameColumn(
    tableName = "Ingredient",
    fromColumnName = "id",
    toColumnName = "newIngredientId"
)
@RenameColumn(
    tableName = "Ingredient",
    fromColumnName = "name",
    toColumnName = "newName"
)
class MyAutoMigrationSpec : AutoMigrationSpec