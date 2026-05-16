package com.example.landerssuperstore.data.repository

import com.google.firebase.firestore.FirebaseFirestore

object EmployeeRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getDrivers(callback: (List<Map<String, Any>>) -> Unit) {
        db.collection("employees")
            .whereEqualTo("Emp_Role", "Delivery")
            .get()
            .addOnSuccessListener { snapshot ->
                val drivers = snapshot.documents.map { it.data ?: emptyMap() }
                callback(drivers)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}
