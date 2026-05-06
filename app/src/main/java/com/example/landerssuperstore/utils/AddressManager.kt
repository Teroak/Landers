package com.example.landerssuperstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AddressManager {
    private val _userAddress = MutableLiveData<String?>(null)
    val userAddress: LiveData<String?> = _userAddress

    fun setAddress(address: String) {
        _userAddress.value = address
    }

    fun getAddress(): String? {
        return _userAddress.value
    }

    fun hasAddress(): Boolean {
        return !_userAddress.value.isNullOrBlank()
    }
}
