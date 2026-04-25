package com.example.genericapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        // Home
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Orders
        findViewById<LinearLayout>(R.id.nav_orders).setOnClickListener {
            // Already in Orders
        }

        // Categories
        findViewById<LinearLayout>(R.id.nav_categories).setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        // Account
        findViewById<LinearLayout>(R.id.nav_account).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}