package com.example.landerssuperstore.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityAdminDashboardBinding
import com.example.landerssuperstore.ui.login.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDashboard()
    }

    private fun setupDashboard() {
        binding.buttonLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        
        binding.buttonManageProducts.setOnClickListener {
            startActivity(Intent(this, ManageProductsActivity::class.java))
        }
        
        binding.buttonManageOrders.setOnClickListener {
            startActivity(Intent(this, ManageOrdersActivity::class.java))
        }
        
        binding.buttonManageUsers.setOnClickListener {
            startActivity(Intent(this, ManageUsersActivity::class.java))
        }
        
        binding.buttonAnalytics.setOnClickListener {
            startActivity(Intent(this, AnalyticsDashboardActivity::class.java))
        }
    }
}
