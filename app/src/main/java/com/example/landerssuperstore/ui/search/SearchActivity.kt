package com.example.landerssuperstore.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.data.repository.ProductRepository
import com.example.landerssuperstore.databinding.ActivitySearchBinding
import com.example.landerssuperstore.ui.adapters.ProductGridAdapter
import com.example.landerssuperstore.ui.productdetails.ProductDetailsActivity
import com.example.landerssuperstore.utils.CartManager

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        val spanCount = if (resources.configuration.screenWidthDp >= 600) 3 else 2
        binding.recyclerResults.layoutManager = GridLayoutManager(this, spanCount)

        binding.editSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim() ?: ""
            if (query.length >= 2) {
                val results = ProductRepository.searchProducts(query)
                binding.recyclerResults.adapter = ProductGridAdapter(results, ::onProductClick, ::onAddToCart)
                binding.textEmpty.visibility = if (results.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            } else {
                binding.recyclerResults.adapter = ProductGridAdapter(emptyList(), ::onProductClick, ::onAddToCart)
                binding.textEmpty.visibility = android.view.View.GONE
            }
        }
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
