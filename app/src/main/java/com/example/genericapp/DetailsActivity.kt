package com.example.genericapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)

        // Receiving data from Intent
        val name = intent.getStringExtra("PRODUCT_NAME")
        val price = intent.getStringExtra("PRODUCT_PRICE")
        val desc = intent.getStringExtra("PRODUCT_DESC")

        // Displaying the data
        findViewById<TextView>(R.id.detail_name).text = "Name: $name"
        findViewById<TextView>(R.id.detail_price).text = "Price: $price"
        findViewById<TextView>(R.id.detail_description).text = "Description: $desc"
    }
}