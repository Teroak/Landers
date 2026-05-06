package com.example.landerssuperstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.landerssuperstore.data.model.CartItem
import com.example.landerssuperstore.data.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrderManager {

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders

    fun addOrder(
        items: List<CartItem>,
        subtotal: Double,
        deliveryFee: Double,
        discountAmount: Double,
        total: Double,
        paymentMethod: String,
        deliveryMethod: String,
        branchName: String,
        address: String,
        voucherCode: String?
    ) {
        val currentList = _orders.value ?: mutableListOf()
        val order = Order(
            id = "ORD-${System.currentTimeMillis()}",
            items = items.toList(),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discountAmount = discountAmount,
            total = total,
            date = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date()),
            status = "Pending",
            paymentMethod = paymentMethod,
            deliveryMethod = deliveryMethod,
            branchName = branchName,
            deliveryAddress = address,
            voucherCode = voucherCode,
            assignedRider = if (deliveryMethod != "Pickup") "To be assigned" else null
        )
        currentList.add(0, order)
        _orders.value = currentList
    }

    fun getOrdersByStatus(status: String): List<Order> {
        val list = _orders.value ?: mutableListOf()
        return if (status == "All") list else list.filter { it.status == status }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        val currentList = _orders.value ?: mutableListOf()
        val order = currentList.find { it.id == orderId }
        if (order != null) {
            order.status = newStatus
            // Simulate assigning a rider when status changes to Processing/Out for Delivery
            if ((newStatus == "Processing" || newStatus == "Out for Delivery") && 
                order.deliveryMethod != "Pickup" && 
                (order.assignedRider == null || order.assignedRider == "To be assigned")) {
                val riders = listOf("Mark Anthony", "John Lloyd", "Ricardo Dalisay", "Dingdong Dantes")
                order.assignedRider = riders.random()
            }
            _orders.value = currentList
        }
    }
}
