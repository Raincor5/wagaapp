// DishIngredientsViewModel.kt
package com.wagaapp.foodguide

import androidx.lifecycle.*
import com.wagaapp.foodguide.data.dao.*
import com.wagaapp.foodguide.data.entitites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DishIngredientsViewModel(
    private val dishDao: DishDao,
    private val ingredientDao: IngredientDao,
    private val dishIngredientCrossRefDao: DishIngredientCrossRefDao
) : ViewModel() {

    private val _dish = MutableLiveData<Dish?>()
    val dish: LiveData<Dish?> get() = _dish

    private val _dishIngredients = MutableLiveData<List<DishIngredientCrossRef>>()
    val dishIngredients: LiveData<List<DishIngredientCrossRef>> get() = _dishIngredients

    private val _ingredients = MutableLiveData<List<Ingredient>>()
    val ingredients: LiveData<List<Ingredient>> get() = _ingredients

    fun loadDish(dishId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedDish = dishDao.getDishById(dishId)
            withContext(Dispatchers.Main) {
                _dish.value = loadedDish
            }
        }
    }

    fun loadDishIngredients(dishId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedDishIngredients = dishIngredientCrossRefDao.getIngredientsForDish(dishId)
            withContext(Dispatchers.Main) {
                _dishIngredients.value = loadedDishIngredients
            }
        }
    }

    fun loadIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedIngredients = ingredientDao.getAllIngredients()
            withContext(Dispatchers.Main) {
                _ingredients.value = loadedIngredients
            }
        }
    }
}

class DishIngredientsViewModelFactory(
    private val dishDao: DishDao,
    private val ingredientDao: IngredientDao,
    private val dishIngredientCrossRefDao: DishIngredientCrossRefDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishIngredientsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishIngredientsViewModel(dishDao, ingredientDao, dishIngredientCrossRefDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}