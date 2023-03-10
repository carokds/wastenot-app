package com.hoang.wastenot.api

import com.google.firebase.firestore.local.LruGarbageCollector
import com.google.gson.annotations.SerializedName


data class RecipeRootResponse (val results: ArrayList<RecipeResponse>)


data class RecipeResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("image") var image: String? = null,


    )