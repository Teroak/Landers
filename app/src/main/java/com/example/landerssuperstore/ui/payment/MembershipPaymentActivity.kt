package com.example.landerssuperstore.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityMembershipPaymentBinding
import com.example.landerssuperstore.utils.MembershipManager

class MembershipPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMembershipPaymentBinding
    private var membershipTier = "basic" // default to basic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembershipPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get membership tier from intent
        membershipTier = intent.getStringExtra("MEMBERSHIP_TIER") ?: "basic"
        
        // Update UI based on membership tier
        updatePaymentUI()

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonPay.setOnClickListener {
            // Validate credit card information
            val cardNumber = binding.editCardNumber.text.toString().trim()
            val expiryDate = binding.editExpiryDate.text.toString().trim()
            val cvv = binding.editCvv.text.toString().trim()
            val cardholderName = binding.editCardholderName.text.toString().trim()

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || cardholderName.isEmpty()) {
                Toast.makeText(this, "Please fill in all credit card fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simple validation - in real app, use proper credit card validation
            if (!isValidCreditCardNumber(cardNumber)) {
                Toast.makeText(this, "Invalid credit card number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidExpiryDate(expiryDate)) {
                Toast.makeText(this, "Invalid expiry date (MM/YY)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cvv.length != 3) {
                Toast.makeText(this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Process payment
            processMembershipPayment()
        }
    }

    private fun updatePaymentUI() {
        when (membershipTier) {
            "basic" -> {
                binding.buttonPay.text = "PAY ₱600"
                binding.textPaymentTitle.text = "Basic Membership Payment"
            }
            "premium" -> {
                binding.buttonPay.text = "PAY ₱900"
                binding.textPaymentTitle.text = "Premium Membership Payment"
            }
        }
    }

    private fun isValidCreditCardNumber(cardNumber: String): Boolean {
        // Simple validation - remove spaces and check length
        val cleaned = cardNumber.replace(" ", "")
        return cleaned.length in 15..19 && cleaned.all { it.isDigit() }
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        // Format: MM/YY
        val parts = expiryDate.split("/")
        if (parts.size != 2) return false
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false
        return month in 1..12 && year in 24..99 // Assuming current year is 2024
    }

    private fun processMembershipPayment() {
        // In a real app, this would integrate with a payment gateway
        // For now, simulate successful payment
        MembershipManager.setMembershipStatus(true)
        Toast.makeText(this, "Membership payment processed successfully!", Toast.LENGTH_SHORT).show()
        
        // Navigate back to main activity
        finish()
    }
}