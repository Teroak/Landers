package com.example.landerssuperstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discountAmount: Double,
    val total: Double,
    val date: String,
    var status: String,
    val paymentMethod: String,
    val deliveryMethod: String,
    val branchName: String,
    val deliveryAddress: String,
    val voucherCode: String? = null,
    var assignedRider: String? = null
) : Parcelable
