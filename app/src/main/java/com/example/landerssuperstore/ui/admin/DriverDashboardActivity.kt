package com.example.landerssuperstore.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.R
import com.example.landerssuperstore.data.model.Order
import com.example.landerssuperstore.data.repository.OrderRepository
import com.example.landerssuperstore.databinding.ActivityDriverDashboardBinding
import com.example.landerssuperstore.ui.adapters.DriverOrdersAdapter
import com.example.landerssuperstore.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class DriverDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverDashboardBinding
    private lateinit var adapter: DriverOrdersAdapter
    private var ordersListener: com.google.firebase.firestore.ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val driverName = intent.getStringExtra("USER_NAME") ?: "Driver"
        binding.textWelcome.text = getString(R.string.welcome_driver, driverName)

        setupRecyclerView()
        startObservingOrders()

        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = DriverOrdersAdapter(emptyList()) { order, newStatus ->
            OrderRepository.updateOrderStatus(order.id, newStatus) { success ->
                if (success) {
                    Toast.makeText(this, "Order status updated to $newStatus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrders.adapter = adapter
    }

    private fun startObservingOrders() {
        val currentDriverId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        
        ordersListener = OrderRepository.observeAllOrders { ordersMapList ->
            val orders = ordersMapList
                .filter { (it["Driver_ID"] as? String) == currentDriverId }
                .map { map ->
                    val dateStr = try {
                        val timestamp = map["Order_Date"] as? com.google.firebase.Timestamp
                        if (timestamp != null) {
                            val sdf = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
                            sdf.format(timestamp.toDate())
                        } else ""
                    } catch (_: Exception) { "" }

                    Order(
                        id = map["Order_ID"] as? String ?: "",
                        items = emptyList(),
                        subtotal = (map["Order_Subtotal"] as? Number)?.toDouble() ?: 0.0,
                        deliveryFee = (map["Order_DeliveryFee"] as? Number)?.toDouble() ?: 0.0,
                        discountAmount = (map["Order_DiscountAmount"] as? Number)?.toDouble() ?: 0.0,
                        total = (map["Order_TotalAmount"] as? Number)?.toDouble() ?: 0.0,
                        date = dateStr,
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

    override fun onDestroy() {
        super.onDestroy()
        ordersListener?.remove()
    }
}
