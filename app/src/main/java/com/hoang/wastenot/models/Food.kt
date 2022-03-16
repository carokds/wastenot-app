package com.hoang.wastenot.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    var id: String,
    val name: String,
    val expirationDate: Timestamp,
    val pictureUrl: String?,
    val ownerEmail: String,
) : Parcelable {
    constructor() : this("", "", Timestamp.now(), "", "")
}
