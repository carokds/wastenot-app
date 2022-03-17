package com.hoang.wastenot.api

import com.google.gson.annotations.SerializedName

data class BarcodeResponse(
    @SerializedName("code") var code: String? = null,
    @SerializedName("product") var product: Product? = Product()
)

data class Product(
    @SerializedName("id") var id: String? = null,
    @SerializedName("brands") var brands: String? = null,
    @SerializedName("categories_tags") var categoriesTags: ArrayList<String> = arrayListOf(),
    @SerializedName("image_front_url") var imageFrontUrl: String? = null,
    @SerializedName("generic_name") var genericName: String? = null,
    @SerializedName("product_name") var productName: String? = null,
    @SerializedName("quantity") var quantity: String? = null
)