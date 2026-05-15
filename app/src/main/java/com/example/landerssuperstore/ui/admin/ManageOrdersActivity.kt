package com.example.landerssuperstore.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.data.repository.OrderRepository
import com.example.landerssuperstore.data.repository.EmployeeRepository
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
        fetchOrders()
        
        binding.buttonBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = ManageOrdersAdapter(emptyList(), { order ->
            showUpdateStatusDialog(order)
        }, { order ->
            showAssignDriverDialog(order)
        })
        binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrders.adapter = adapter
    }

    private fun fetchOrders() {
        OrderRepository.getAllOrders { ordersMapList ->
            val orders = ordersMapList.map { map ->
                Order(
                    id = map["Order_ID"] as? String ?: "",
                    items = emptyList(), // Simplified for now
                    subtotal = (map["Order_TotalAmount"] as? Number)?.toDouble() ?: 0.0,
                    deliveryFee = (map["Order_DeliveryFee"] as? Number)?.toDouble() ?: 0.0,
                    discountAmount = 0.0,
                    total = (map["Order_TotalAmount"] as? Number)?.toDouble() ?: 0.0,
                    date = "", // Simplified
                    status = map["Order_Status"] as? String ?: "Pending",
                    paymentMethod = map["Order_PaymentMethod"] as? String ?: "Cash",
                    deliveryMethod = map["Order_DeliveryMethod"] as? String ?: "Standard",
                    branchName = map["Branch_Name"] as? String ?: "Main",
                    deliveryAddress = map["Order_DeliveryAddr"] as? String ?: "",
                    assignedRider = map["Driver_Name"] as? String
                )
            }
            runOnUiThread {
                adapter.updateList(orders)
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
            OrderRepository.updateOrderStatus(order.id, newStatus) { success ->
                if (success) {
                    Toast.makeText(this, "Order status updated to $newStatus", Toast.LENGTH_SHORT).show()
                    fetchOrders()
                } else {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showAssignDriverDialog(order: Order) {
        EmployeeRepository.getDrivers { drivers ->
            if (drivers.isEmpty()) {
                Toast.makeText(this, "No drivers available", Toast.LENGTH_SHORT).show()
                return@getDrivers
            }

            val driverNames = drivers.map { it["Emp_FirstName"].toString() + " " + it["Emp_LastName"].toString() }.toTypedArray()
            val driverIds = drivers.map { it["Emp_ID"].toString() }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Assign Driver")
            builder.setItems(driverNames) { _, which ->
                val selectedDriverId = driverIds[which]
                val selectedDriverName = driverNames[which]
                OrderRepository.assignDriver(order.id, selectedDriverId, selectedDriverName) { success ->
                    if (success) {
                        Toast.makeText(this, "Driver assigned: $selectedDriverName", Toast.LENGTH_SHORT).show()
                        fetchOrders()
                    } else {
                        Toast.makeText(this, "Failed to assign driver", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("Cancel", null)
            runOnUiThread { builder.show() }
        }
    }
}
