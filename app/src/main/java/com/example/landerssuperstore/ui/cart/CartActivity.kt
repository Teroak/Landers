package com.example.landerssuperstore.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.databinding.ActivityCartBinding
import com.example.landerssuperstore.ui.MainActivity
import com.example.landerssuperstore.ui.adapters.CartAdapter
import com.example.landerssuperstore.utils.CartManager
import com.example.landerssuperstore.utils.OrderManager
import com.example.landerssuperstore.utils.MembershipManager
import com.example.landerssuperstore.utils.AddressManager

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

        CartManager.cartTotal.observe(this) {
            updateMembershipStatus()
        }

        MembershipManager.isMember.observe(this) {
            updateMembershipStatus()
        }

        binding.buttonCheckout.setOnClickListener {
            val items = CartManager.cartItems.value
            if (items.isNullOrEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else if (!MembershipManager.isUserMember()) {
                // Show membership requirement message
                Toast.makeText(this, "You must be a Landers member to checkout. Please join our membership program.", Toast.LENGTH_LONG).show()
                // Navigate to Be a member screen
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("NAVIGATE_TO_MEMBERSHIP", true)
                startActivity(intent)
                finish()
            } else if (!AddressManager.hasAddress()) {
                showAddressDialog()
            } else {
                processCheckout()
            }
        }
        
        // Update membership status indicator
        updateMembershipStatus()
    }

    private fun showAddressDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delivery Address")
        builder.setMessage("Please enter your delivery address to proceed with checkout.")

        val input = EditText(this)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("Save & Checkout") { dialog, _ ->
            val address = input.text.toString().trim()
            if (address.isNotEmpty()) {
                AddressManager.setAddress(address)
                processCheckout()
            } else {
                Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun processCheckout() {
        val items = CartManager.cartItems.value ?: return
        val total = CartManager.cartTotal.value ?: 0.0
        OrderManager.addOrder(items.toList(), total)
        Toast.makeText(this, "Order placed successfully! Delivering to: ${AddressManager.getAddress()}", Toast.LENGTH_LONG).show()
        CartManager.clearCart()
        finish()
    }

    private fun updateMembershipStatus() {
        val isMember = MembershipManager.isUserMember()
        
        if (isMember) {
            // Add visual indicator in cart UI
            // For example, show a membership badge or change text color
            binding.textTotal.text = "₱%,.2f (Member Price)".format(CartManager.cartTotal.value ?: 0.0)
        } else {
            binding.textTotal.text = "₱%,.2f".format(CartManager.cartTotal.value ?: 0.0)
        }
    }
}
