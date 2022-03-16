package com.hoang.wastenot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    var id: String,
    val name: String,
    val expirationDate: String,
    val pictureUrl: String?,
    val ownerEmail: String,
) : Parcelable {
    constructor() : this("", "", "", "", "")
}
