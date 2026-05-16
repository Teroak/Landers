package com.example.landerssuperstore.utils

import android.util.Log
import com.example.landerssuperstore.data.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseInitializer {
    private val db = FirebaseFirestore.getInstance()

    fun seedDatabase(onComplete: (Boolean) -> Unit) {
        val products = ProductRepository.getAllProducts()
        val batch = db.batch()

        products.forEach { product ->
            val productRef = db.collection("products").document(product.id.toString())
            val productMap = hashMapOf(
                "id" to product.id,
                "name" to product.name,
                "price" to product.price,
                "originalPrice" to product.originalPrice,
                "description" to product.description,
                "category" to product.category,
                "imageRes" to product.imageRes,
                "discount" to product.discount,
                "brand" to product.brand,
                "stockQty" to product.stockQty,
                "rating" to product.rating,
                "reviewCount" to product.reviewCount
            )
            batch.set(productRef, productMap)
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d("FirebaseInitializer", "Database seeded successfully")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseInitializer", "Error seeding database", e)
                onComplete(false)
            }
    }
}
