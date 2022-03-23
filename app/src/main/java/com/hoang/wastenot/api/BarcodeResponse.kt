package com.hoang.wastenot.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarcodeResponse(
    @SerializedName("code") var code: String,
    @SerializedName("product") var product: Product
) : Parcelable

@Parcelize
data class Product(
    @SerializedName("id") var id: String,
    @SerializedName("brands") var brands: String,
    @SerializedName("categories_tags") var categoriesTags: ArrayList<String>,
    @SerializedName("image_front_url") var imageFrontUrl: String,
    @SerializedName("generic_name") var genericName: String,
    @SerializedName("product_name") var productName: String
) : Parcelable