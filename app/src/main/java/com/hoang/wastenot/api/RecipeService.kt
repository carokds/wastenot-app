package com.hoang.wastenot.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {
    @GET("findByIngredients?")
    fun getRecipes(
        @Query("ingredients") ingredient: String,
        @Query("number") number: Int,
        @Query("apiKey") API_KEY: String
    ): Call<List<RecipeResponse>>
}

object Retrofit {
    private val baseUrl = "https://api.spoonacular.com/recipes/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
}