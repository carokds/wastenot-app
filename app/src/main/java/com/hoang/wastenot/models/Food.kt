package com.hoang.wastenot.models

data class Food(
    var id: String,
    val name: String,
    val expirationDate: String,
    val pictureUrl: String,
    val ownerEmail: String,
)
