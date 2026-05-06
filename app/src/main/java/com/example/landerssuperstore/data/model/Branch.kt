package com.example.landerssuperstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch(
    val id: Int,
    val name: String,
    val address: String,
    val city: String
) : Parcelable
