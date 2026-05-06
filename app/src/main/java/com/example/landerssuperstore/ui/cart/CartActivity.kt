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
import com.example.landerssuperstore.utils.BranchManager
import com.example.landerssuperstore.data.model.Voucher
import com.example.landerssuperstore.data.repository.VoucherRepository

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var appliedVoucher: Voucher? = null

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

        binding.radioGroupDelivery.setOnCheckedChangeListener { _, _ ->
            updateMembershipStatus()
        }

        binding.buttonApplyVoucher.setOnClickListener {
            val code = binding.editVoucher.text.toString().trim()
            if (code.isEmpty()) {
                appliedVoucher = null
                updateMembershipStatus()
                return@setOnClickListener
            }
            
            val voucher = VoucherRepository.getVoucher(code)
            if (voucher != null) {
                val subtotal = CartManager.cartTotal.value ?: 0.0
                if (subtotal >= voucher.minOrderAmount) {
                    appliedVoucher = voucher
                    Toast.makeText(this, "Voucher Applied!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Min order for this voucher is ₱${voucher.minOrderAmount}", Toast.LENGTH_SHORT).show()
                    appliedVoucher = null
                }
            } else {
                Toast.makeText(this, "Invalid Voucher Code", Toast.LENGTH_SHORT).show()
                appliedVoucher = null
            }
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
            } else if (!AddressManager.hasAddress() && binding.radioPickup.isChecked.not()) {
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
        val subtotal = CartManager.cartTotal.value ?: 0.0
        
        val deliveryFee = when {
            binding.radioSameDay.isChecked -> 99.0
            binding.radioStandard.isChecked -> 49.0
            else -> 0.0
        }
        
        var discountAmount = 0.0
        appliedVoucher?.let {
            discountAmount = if (it.discountType == "Percentage") {
                subtotal * (it.discountValue / 100.0)
            } else {
                it.discountValue
            }
        }
        
        val total = subtotal + deliveryFee - discountAmount
        
        val paymentMethod = when {
            binding.radioGCash.isChecked -> "GCash"
            binding.radioCard.isChecked -> "Card"
            else -> "Cash"
        }
        
        val deliveryMethod = when {
            binding.radioSameDay.isChecked -> "Same-Day"
            binding.radioPickup.isChecked -> "Pickup"
            else -> "Standard"
        }
        
        val branch = BranchManager.selectedBranch.value?.name ?: "Landers Otis"
        val address = if (deliveryMethod == "Pickup") "Pickup at $branch" else AddressManager.getAddress() ?: ""

        OrderManager.addOrder(
            items = items.toList(),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discountAmount = discountAmount,
            total = total,
            paymentMethod = paymentMethod,
            deliveryMethod = deliveryMethod,
            branchName = branch,
            address = address,
            voucherCode = appliedVoucher?.code
        )
        
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show()
        CartManager.clearCart()
        finish()
    }

    private fun updateMembershipStatus() {
        val subtotal = CartManager.cartTotal.value ?: 0.0
        val isMember = MembershipManager.isUserMember()
        
        val deliveryFee = when {
            binding.radioSameDay.isChecked -> 99.0
            binding.radioStandard.isChecked -> 49.0
            else -> 0.0
        }
        
        var discountAmount = 0.0
        appliedVoucher?.let {
            discountAmount = if (it.discountType == "Percentage") {
                subtotal * (it.discountValue / 100.0)
            } else {
                it.discountValue
            }
        }
        
        val finalTotal = subtotal + deliveryFee - discountAmount
        
        if (isMember) {
            binding.textTotal.text = "₱%,.2f (Member Price)".format(finalTotal)
        } else {
            binding.textTotal.text = "₱%,.2f".format(finalTotal)
        }
    }
}
