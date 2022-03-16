package com.hoang.wastenot.repositories

import android.util.Log
import com.hoang.wastenot.api.RecipeDetailResponse
import com.hoang.wastenot.api.RecipeResponse
import com.hoang.wastenot.api.Retrofit.recipeService


class RecipeRepository {

    suspend fun getRecipes(): List<RecipeResponse>? {
        val response =
            recipeService.getRecipes("tomato", 10, "35ffd0936a0b48209553394c38c0b8bd").execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }
    }

    suspend fun getRecipeUrl(recipeId: Int): RecipeDetailResponse?{
        val response =
            recipeService.getRecipeUrl(recipeId, false, "35ffd0936a0b48209553394c38c0b8bd").execute()
        if(response.isSuccessful) {
            return response.body()
        }else{
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }

    }
}
