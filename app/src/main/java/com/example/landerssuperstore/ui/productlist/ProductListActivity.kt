package com.example.landerssuperstore.ui.productlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.data.repository.ProductRepository
import com.example.landerssuperstore.databinding.ActivityProductListBinding
import com.example.landerssuperstore.ui.adapters.ProductGridAdapter
import com.example.landerssuperstore.ui.productdetails.ProductDetailsActivity
import com.example.landerssuperstore.utils.CartManager

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Products"
        binding.textTitle.text = categoryName

        binding.buttonBack.setOnClickListener { finish() }

        val products = ProductRepository.getProductsByCategory(categoryName)
        val spanCount = if (resources.configuration.screenWidthDp >= 600) 3 else 2
        binding.recyclerProducts.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerProducts.adapter = ProductGridAdapter(products, ::onProductClick, ::onAddToCart)
    }

    private fun onProductClick(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }

    private fun onAddToCart(product: Product) {
        CartManager.addToCart(product)
        Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }
}
