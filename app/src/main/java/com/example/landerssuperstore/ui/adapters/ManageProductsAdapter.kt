package com.example.landerssuperstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.databinding.ItemManageProductBinding

class ManageProductsAdapter(
    private var products: List<Product>,
    private val onUpdateStock: (Product) -> Unit
) : RecyclerView.Adapter<ManageProductsAdapter.ProductViewHolder>() {

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemManageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(private val binding: ItemManageProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textProductName.text = product.name
            binding.textStockQty.text = "Stock: ${product.stockQty}"
            binding.textCategory.text = product.category
            binding.textPrice.text = "₱%,.2f".format(product.price)
            
            binding.buttonUpdateStock.setOnClickListener {
                onUpdateStock(product)
            }
        }
    }
}
