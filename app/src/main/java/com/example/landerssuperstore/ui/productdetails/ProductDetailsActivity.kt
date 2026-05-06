package com.example.landerssuperstore.ui.productdetails

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.databinding.ActivityProductDetailsBinding
import com.example.landerssuperstore.ui.cart.CartActivity
import com.example.landerssuperstore.utils.CartManager

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<Product>("PRODUCT")
        if (product == null) {
            finish()
            return
        }

        val resourceId = resources.getIdentifier(product.imageRes, "drawable", packageName)
        if (resourceId != 0) {
            binding.textEmoji.setImageResource(resourceId)
        }
        binding.textName.text = product.name
        binding.textPrice.text = "₱%,.2f".format(product.price)
        if (product.originalPrice != null) {
            binding.textOriginalPrice.text = "₱%,.2f".format(product.originalPrice)
            binding.textOriginalPrice.visibility = android.view.View.VISIBLE
        } else {
            binding.textOriginalPrice.visibility = android.view.View.GONE
        }
        binding.textDescription.text = product.description
        binding.textRating.text = "%.1f".format(product.rating)
        binding.textReviewCount.text = "(${product.reviewCount} reviews)"

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonAddToCart.setOnClickListener {
            CartManager.addToCart(product)
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        }

        binding.buttonBuyNow.setOnClickListener {
            CartManager.addToCart(product)
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
