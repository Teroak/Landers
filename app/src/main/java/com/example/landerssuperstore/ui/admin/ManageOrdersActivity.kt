package com.example.landerssuperstore.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.utils.OrderManager
import com.example.landerssuperstore.databinding.ActivityManageOrdersBinding
import com.example.landerssuperstore.ui.adapters.ManageOrdersAdapter

class ManageOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageOrdersBinding
    private lateinit var adapter: ManageOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        
        binding.buttonBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        OrderManager.orders.observe(this) { orders ->
            if (::adapter.isInitialized) {
                adapter.updateList(orders)
            } else {
                adapter = ManageOrdersAdapter(orders) { order ->
                    showUpdateStatusDialog(order)
                }
                binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
                binding.recyclerOrders.adapter = adapter
            }
        }
    }

    private fun showUpdateStatusDialog(order: Order) {
        val statuses = arrayOf("Pending", "Processing", "Out for Delivery", "Delivered", "Cancelled")
        val currentSelection = statuses.indexOf(order.status).coerceAtLeast(0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Order Status")
        builder.setSingleChoiceItems(statuses, currentSelection) { dialog, which ->
            val newStatus = statuses[which]
            OrderManager.updateOrderStatus(order.id, newStatus)
            Toast.makeText(this, "Order status updated to $newStatus", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}
