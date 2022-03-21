package com.hoang.wastenot.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoang.wastenot.api.RecipeDetailResponse
import com.hoang.wastenot.api.RecipeResponse
import com.hoang.wastenot.api.RecipeRootResponse
import com.hoang.wastenot.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecipeViewModel : ViewModel() {

    private val recipeRepository = RecipeRepository()
    val recipes = MutableLiveData<RecipeRootResponse>()
//    val recipes : LiveData<RecipeRootResponse> = recipeRepository.recipes.asLiveData()

    val recipeUrl = MutableLiveData<RecipeDetailResponse>()

    fun getRecipes(diet:String) {
        viewModelScope.launch(Dispatchers.Default) {
            val recipesList = recipeRepository.getRecipes(diet)
            recipes.postValue(recipesList!!)

        }
    }

    fun getRecipeUrl(recipeId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val url = recipeRepository.getRecipeUrl(recipeId)
            recipeUrl.postValue(url!!)
        }
    }
}


