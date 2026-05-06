package com.example.landerssuperstore.ui.orders

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order = intent.getParcelableExtra<Order>("ORDER")
        if (order == null) {
            finish()
            return
        }

        binding.buttonBack.setOnClickListener { finish() }
        
        displayOrderDetails(order)
    }

    private fun displayOrderDetails(order: Order) {
        binding.textOrderId.text = "Order ID: ${order.id}"
        binding.textStatus.text = order.status.uppercase()
        
        if (order.deliveryMethod == "Pickup") {
            binding.textRiderInfo.visibility = View.GONE
            binding.textBranchInfo.text = "Pickup at ${order.branchName}"
            binding.textAddress.text = "Selected Branch: ${order.branchName}"
        } else {
            binding.textRiderInfo.text = "Rider: ${order.assignedRider ?: "To be assigned"}"
            binding.textBranchInfo.text = "Arriving from ${order.branchName}"
            binding.textAddress.text = order.deliveryAddress
        }

        binding.textSubtotal.text = "₱%,.2f".format(order.subtotal)
        binding.textDeliveryFee.text = "₱%,.2f".format(order.deliveryFee)
        
        if (order.discountAmount > 0) {
            binding.layoutDiscount.visibility = View.VISIBLE
            binding.textDiscount.text = "-₱%,.2f".format(order.discountAmount)
        } else {
            binding.layoutDiscount.visibility = View.GONE
        }
        
        binding.textTotal.text = "₱%,.2f".format(order.total)
        binding.textPaymentMethod.text = "Paid via ${order.paymentMethod}"
    }
}
