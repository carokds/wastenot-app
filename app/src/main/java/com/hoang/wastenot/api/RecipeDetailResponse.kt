package com.hoang.wastenot.api

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(
    @SerializedName("sourceUrl") var sourceUrl: String? = null,
    )