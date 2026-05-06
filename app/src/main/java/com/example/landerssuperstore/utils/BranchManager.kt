package com.example.landerssuperstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.landerssuperstore.data.model.Branch

object BranchManager {
    private val branches = listOf(
        Branch(1, "Landers Arca South", "Taguig City", "Metro Manila"),
        Branch(2, "Landers Alabang West", "Muntinlupa City", "Metro Manila"),
        Branch(3, "Landers Balintawak", "Quezon City", "Metro Manila"),
        Branch(4, "Landers Otis", "Manila", "Metro Manila"),
        Branch(5, "Landers Cebu", "Cebu City", "Cebu")
    )

    private val _selectedBranch = MutableLiveData<Branch>(branches[0])
    val selectedBranch: LiveData<Branch> = _selectedBranch

    fun getBranches() = branches

    fun selectBranch(branch: Branch) {
        _selectedBranch.value = branch
    }
}
