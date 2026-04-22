package com.example.genericapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.genericapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Navigation logic
        findViewById<LinearLayout>(R.id.nav_orders)?.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_categories)?.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_account)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
