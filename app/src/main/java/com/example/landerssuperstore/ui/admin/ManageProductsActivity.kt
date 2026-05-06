package com.example.landerssuperstore.ui.admin

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.data.repository.ProductRepository
import com.example.landerssuperstore.databinding.ActivityManageProductsBinding
import com.example.landerssuperstore.ui.adapters.ManageProductsAdapter

class ManageProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProductsBinding
    private lateinit var adapter: ManageProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        
        binding.buttonBack.setOnClickListener { finish() }
        
        binding.fabAddProduct.setOnClickListener {
            Toast.makeText(this, "Add product feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        val products = ProductRepository.getAllProducts()
        adapter = ManageProductsAdapter(products) { product ->
            showUpdateStockDialog(product)
        }
        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = adapter
    }

    private fun showUpdateStockDialog(product: Product) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Stock")
        builder.setMessage("Enter new stock quantity for ${product.name}:")

        val input = EditText(this)
        input.setText(product.stockQty.toString())
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("Update") { dialog, _ ->
            val qtyStr = input.text.toString()
            if (qtyStr.isNotEmpty()) {
                val newQty = qtyStr.toInt()
                ProductRepository.updateStock(product.id, newQty)
                adapter.updateList(ProductRepository.getAllProducts())
                Toast.makeText(this, "Stock updated successfully", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
