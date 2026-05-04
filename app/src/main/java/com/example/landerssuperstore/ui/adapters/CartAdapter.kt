package com.example.landerssuperstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landerssuperstore.data.model.CartItem
import com.example.landerssuperstore.databinding.ItemCartBinding

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChanged: (Int, Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.textEmoji.setImageResource(item.product.imageRes)
            binding.textProductName.text = item.product.name
            binding.textPrice.text = "₱%,.2f".format(item.product.price)
            binding.textQuantity.text = item.quantity.toString()
            binding.textTotal.text = "₱%,.2f".format(item.totalPrice)

            binding.buttonMinus.setOnClickListener {
                onQuantityChanged(item.product.id, item.quantity - 1)
            }
            binding.buttonPlus.setOnClickListener {
                onQuantityChanged(item.product.id, item.quantity + 1)
            }
            binding.buttonRemove.setOnClickListener {
                onRemove(item.product.id)
            }
        }
    }
}
