package com.hoang.wastenot.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {
    @GET("complexSearch?")
    fun getRecipes(
        @Query("includeIngredients") ingredient: String,
        @Query("number") number: Int,
        @Query("sort") sort: String,
        @Query("diet") diet: String,
        @Query("apiKey") API_KEY: String
    ): Call<RecipeRootResponse>

    @GET("{id}/information?")
    fun getRecipeUrl(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean,
        @Query("apiKey") API_KEY: String,
    ): Call<RecipeDetailResponse>
}

object Retrofit {
    private val baseUrl = "https://api.spoonacular.com/recipes/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
}