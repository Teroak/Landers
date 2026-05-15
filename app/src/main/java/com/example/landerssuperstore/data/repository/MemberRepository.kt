package com.example.landerssuperstore.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object MemberRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun updateAddress(address: String, callback: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("members").document(userId)
            .update("Mem_Address", address)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun getMemberData(callback: (Map<String, Any>?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("members").document(userId).get()
            .addOnSuccessListener { callback(it.data) }
            .addOnFailureListener { callback(null) }
    }
}
