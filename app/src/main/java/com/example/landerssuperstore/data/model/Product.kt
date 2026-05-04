package com.example.landerssuperstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,
    val description: String,
    val category: String,
    val imageRes: Int,
    val discount: String? = null,
    val rating: Float = 4.5f,
    val reviewCount: Int = 100
) : Parcelable
