package com.example.landerssuperstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.databinding.ItemManageOrderBinding

class ManageOrdersAdapter(
    private var orders: List<Order>,
    private val onUpdateStatus: (Order) -> Unit
) : RecyclerView.Adapter<ManageOrdersAdapter.OrderViewHolder>() {

    fun updateList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemManageOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(private val binding: ItemManageOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.textOrderId.text = "Order #${order.id.takeLast(6)}"
            binding.textDate.text = order.date
            binding.textStatus.text = order.status
            binding.textTotal.text = "Total: ₱%,.2f".format(order.total)
            
            binding.buttonUpdateStatus.setOnClickListener {
                onUpdateStatus(order)
            }
        }
    }
}
