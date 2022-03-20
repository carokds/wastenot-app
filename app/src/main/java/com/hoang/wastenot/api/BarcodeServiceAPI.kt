package com.hoang.wastenot.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BarcodeService {
    @GET("{barcode}.json")
    fun getProductInfo(
        @Path("barcode") barcode: String?): Call<BarcodeResponse>
}

class BarcodeServiceAPI {

    fun getBarcodeService() : BarcodeService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/api/v0/product/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(BarcodeService::class.java)
    }
}

