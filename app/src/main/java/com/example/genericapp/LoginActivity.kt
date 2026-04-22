package com.example.genericapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landers)

        // LOG IN Button
        findViewById<Button>(R.id.button_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.edit_email).text.toString().trim()
            val password = findViewById<EditText>(R.id.edit_password).text.toString().trim()

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // ✅ Extract name from email before @
                    // e.g. "juandelacruz@gmail.com" → "juandelacruz"
                    val name = email.substringBefore("@")

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    intent.putExtra("USER_NAME", name)
                    startActivity(intent)
                }
            }
        }

        // Sign Up Tab → go to Register
        findViewById<TextView>(R.id.tab_signup).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}