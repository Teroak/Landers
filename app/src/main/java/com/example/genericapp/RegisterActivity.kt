package com.example.genericapp
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        // Go back to Login
        findViewById<TextView>(R.id.text_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Register button → go to Login
        findViewById<Button>(R.id.button_register).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}