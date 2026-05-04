package com.example.landerssuperstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.landerssuperstore.data.model.CartItem
import com.example.landerssuperstore.data.model.Product

object CartManager {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    private val _cartCount = MutableLiveData(0)
    val cartCount: LiveData<Int> = _cartCount

    private val _cartTotal = MutableLiveData(0.0)
    val cartTotal: LiveData<Double> = _cartTotal

    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        val existing = currentList.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity += 1
        } else {
            currentList.add(CartItem(product))
        }
        _cartItems.value = currentList
        updateTotals()
    }

    fun removeFromCart(productId: Int) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.removeAll { it.product.id == productId }
        _cartItems.value = currentList
        updateTotals()
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        val currentList = _cartItems.value ?: mutableListOf()
        val item = currentList.find { it.product.id == productId }
        if (item != null) {
            if (quantity <= 0) {
                currentList.remove(item)
            } else {
                item.quantity = quantity
            }
            _cartItems.value = currentList
            updateTotals()
        }
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
        updateTotals()
    }

    fun isInCart(productId: Int): Boolean {
        return _cartItems.value?.any { it.product.id == productId } == true
    }

    private fun updateTotals() {
        val list = _cartItems.value ?: mutableListOf()
        _cartCount.value = list.sumOf { it.quantity }
        _cartTotal.value = list.sumOf { it.totalPrice }
    }
}
