package com.hoang.wastenot.repositories

import android.util.Log
import com.hoang.wastenot.api.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BarcodeRepository : KoinComponent {

    private val serviceAPI : BarcodeServiceAPI by inject()

    private val barcodeService : BarcodeService = serviceAPI.getBarcodeService()

    suspend fun getBarcodeResponse(barcode: String?): BarcodeResponse? {
        val response = barcodeService.getProductInfo(barcode).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            null
        }
    }
}