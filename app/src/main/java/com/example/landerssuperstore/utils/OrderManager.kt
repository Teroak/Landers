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

    fun addOrder(items: List<CartItem>, total: Double) {
        val currentList = _orders.value ?: mutableListOf()
        val order = Order(
            id = "ORD-${System.currentTimeMillis()}",
            items = items.toList(),
            total = total,
            date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
            status = "Pending"
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
            _orders.value = currentList
        }
    }
}
