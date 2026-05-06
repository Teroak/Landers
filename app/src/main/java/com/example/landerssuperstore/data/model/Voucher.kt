package com.example.landerssuperstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher(
    val id: Int,
    val code: String,
    val discountType: String, // "Percentage" or "Fixed"
    val discountValue: Double,
    val minOrderAmount: Double
) : Parcelable
