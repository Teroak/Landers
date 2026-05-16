package com.example.landerssuperstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.databinding.ItemDriverOrderBinding

class DriverOrdersAdapter(
    private var orders: List<Order>,
    private val onUpdateStatus: (Order, String) -> Unit
) : RecyclerView.Adapter<DriverOrdersAdapter.OrderViewHolder>() {

    fun updateList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemDriverOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(private val binding: ItemDriverOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            val context = binding.root.context
            binding.textOrderId.text = context.getString(com.example.landerssuperstore.R.string.order_prefix, order.id.takeLast(6))
            binding.textStatus.text = order.status
            binding.textAddress.text = order.deliveryAddress
            
            binding.buttonOutForDelivery.setOnClickListener {
                onUpdateStatus(order, "Out for Delivery")
            }

            binding.buttonDelivered.setOnClickListener {
                onUpdateStatus(order, "Delivered")
            }
        }
    }
}
