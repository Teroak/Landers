package com.example.landerssuperstore.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonAdminLogin.setOnClickListener {
            val email = binding.editAdminEmail.text.toString().trim()
            val password = binding.editAdminPassword.text.toString().trim()

            // Static admin credentials
            if (email == "admin@landers.ph" && password == "admin123") {
                val intent = Intent(this, AdminDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter admin credentials", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid admin credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
