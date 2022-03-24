package com.hoang.wastenot.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoang.wastenot.api.BarcodeResponse
import com.hoang.wastenot.repositories.BarcodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BarcodeSharedViewModel : ViewModel(), KoinComponent {

    private val repository: BarcodeRepository by inject()

    val product = MutableLiveData<BarcodeResponse>()

    val showBottomSheetDialog = MutableLiveData<Boolean>()

    fun getInfo(barcode: String?) = viewModelScope.launch(Dispatchers.Default) {
        val barcodeResponse = repository.getBarcodeResponse(barcode) as BarcodeResponse
        withContext(Dispatchers.Main){
            product.value = barcodeResponse
            showBottomSheetDialog.value = true
        }
    }

    fun hideBottomSheetDialog(){
        showBottomSheetDialog.value = false
    }

}