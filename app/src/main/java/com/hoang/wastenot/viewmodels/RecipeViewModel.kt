package com.hoang.wastenot.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoang.wastenot.api.RecipeResponse
import com.hoang.wastenot.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecipeViewModel: ViewModel() {

    private val recipeRepository = RecipeRepository()
    val recipes = MutableLiveData<List<RecipeResponse>>()

    fun getRecipes(){
        viewModelScope.launch(Dispatchers.Default){
            val recipesList = recipeRepository.getRecipes()
            recipes.postValue(recipesList!!)

        }
    }
}

private fun <T> MutableLiveData<T>.postValue(recipesList: List<T>) {

}