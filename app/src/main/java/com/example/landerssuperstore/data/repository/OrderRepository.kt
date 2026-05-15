package com.example.landerssuperstore.data.repository

import com.example.landerssuperstore.data.model.CartItem
import com.example.landerssuperstore.data.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun placeOrder(
        items: List<CartItem>,
        subtotal: Double,
        deliveryFee: Double,
        discountAmount: Double,
        total: Double,
        paymentMethod: String,
        deliveryMethod: String,
        branchName: String,
        address: String,
        voucherCode: String?,
        callback: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val orderId = "ORD-${System.currentTimeMillis()}"
        
        val orderMap = hashMapOf(
            "Order_ID" to orderId,
            "Mem_ID" to userId,
            "Order_Date" to Date(),
            "Order_DeliveryAddr" to address,
            "Order_DeliveryMethod" to deliveryMethod,
            "Order_Status" to "Pending",
            "Order_TotalAmount" to total,
            "Order_DeliveryFee" to deliveryFee,
            "Order_PaymentMethod" to paymentMethod,
            "Branch_Name" to branchName,
            "Vou_Code" to voucherCode,
            "Items" to items.map { it.product.name + " x" + it.quantity }
        )

        db.collection("orders").document(orderId)
            .set(orderMap)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun getMyOrders(callback: (List<Map<String, Any>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("orders")
            .whereEqualTo("Mem_ID", userId)
            .orderBy("Order_Date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val orders = querySnapshot.documents.map { it.data ?: emptyMap() }
                callback(orders)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun getAllOrders(callback: (List<Map<String, Any>>) -> Unit) {
        db.collection("orders")
            .orderBy("Order_Date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val orders = querySnapshot.documents.map { it.data ?: emptyMap() }
                callback(orders)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun updateOrderStatus(orderId: String, status: String, callback: (Boolean) -> Unit) {
        db.collection("orders").document(orderId)
            .update("Order_Status", status)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun assignDriver(orderId: String, driverId: String, driverName: String, callback: (Boolean) -> Unit) {
        val updates = hashMapOf<String, Any>(
            "Driver_ID" to driverId,
            "Driver_Name" to driverName,
            "Order_Status" to "Processing"
        )
        db.collection("orders").document(orderId)
            .update(updates)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }
}
