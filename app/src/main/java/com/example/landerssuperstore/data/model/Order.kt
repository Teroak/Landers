package com.example.landerssuperstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val date: String,
    var status: String
) : Parcelable
