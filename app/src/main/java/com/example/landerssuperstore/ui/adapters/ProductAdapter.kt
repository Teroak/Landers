package com.example.landerssuperstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.databinding.ItemProductBinding

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val resourceId = binding.root.context.resources.getIdentifier(product.imageRes, "drawable", binding.root.context.packageName)
            if (resourceId != 0) {
                binding.textEmoji.setImageResource(resourceId)
            }
            binding.textProductName.text = product.name
            binding.textPrice.text = "₱%,.2f".format(product.price)
            binding.textDiscount.visibility = if (product.discount != null) {
                binding.textDiscount.text = product.discount
                android.view.View.VISIBLE
            } else android.view.View.GONE

            binding.root.setOnClickListener { onProductClick(product) }
            binding.buttonAdd.setOnClickListener { onAddToCart(product) }
        }
    }
}
