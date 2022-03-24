package com.hoang.wastenot.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.hoang.wastenot.BuildConfig
import com.hoang.wastenot.api.RecipeDetailResponse
import com.hoang.wastenot.api.RecipeResponse
import com.hoang.wastenot.api.RecipeRootResponse
import com.hoang.wastenot.api.Retrofit.recipeService
import kotlinx.coroutines.flow.Flow


class RecipeRepository {

    val API_KEY = BuildConfig.SPOONACULAR_API_KEY

    suspend fun getRecipes(ingredient: String, diet: String): RecipeRootResponse? {
        val response =
            recipeService.getRecipes(
                ingredient,
                10,
                "min-missing-ingredients",
                diet,
                API_KEY
            ).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }
    }

    suspend fun getRecipeUrl(recipeId: Int): RecipeDetailResponse? {
        val response =
            recipeService.getRecipeUrl(recipeId, false, API_KEY)
                .execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }

    }
}
