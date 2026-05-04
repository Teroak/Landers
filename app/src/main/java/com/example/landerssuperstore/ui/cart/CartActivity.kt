package com.example.landerssuperstore.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.databinding.ActivityCartBinding
import com.example.landerssuperstore.ui.adapters.CartAdapter
import com.example.landerssuperstore.utils.CartManager
import com.example.landerssuperstore.utils.OrderManager

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        cartAdapter = CartAdapter(emptyList(), { productId, quantity ->
            CartManager.updateQuantity(productId, quantity)
        }, { productId ->
            CartManager.removeFromCart(productId)
        })

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = cartAdapter

        CartManager.cartItems.observe(this) { items ->
            cartAdapter.updateList(items)
            if (items.isEmpty()) {
                binding.textEmpty.visibility = android.view.View.VISIBLE
                binding.recyclerCart.visibility = android.view.View.GONE
                binding.layoutBottom.visibility = android.view.View.GONE
            } else {
                binding.textEmpty.visibility = android.view.View.GONE
                binding.recyclerCart.visibility = android.view.View.VISIBLE
                binding.layoutBottom.visibility = android.view.View.VISIBLE
            }
        }

        CartManager.cartTotal.observe(this) { total ->
            binding.textTotal.text = "₱%,.2f".format(total)
        }

        binding.buttonCheckout.setOnClickListener {
            val items = CartManager.cartItems.value
            if (items.isNullOrEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val total = CartManager.cartTotal.value ?: 0.0
                OrderManager.addOrder(items.toList(), total)
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                CartManager.clearCart()
                finish()
            }
        }
    }
}
