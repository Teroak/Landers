package com.example.landerssuperstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object MembershipManager {
    private val _isMember = MutableLiveData<Boolean>(false)
    val isMember: LiveData<Boolean> = _isMember

    fun setMembershipStatus(isMember: Boolean) {
        _isMember.value = isMember
    }

    fun isUserMember(): Boolean {
        return _isMember.value ?: false
    }
}