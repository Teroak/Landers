package com.example.landerssuperstore.ui.account

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonSave.setOnClickListener {
            val current = binding.editCurrentPassword.text.toString().trim()
            val newPassword = binding.editNewPassword.text.toString().trim()
            val confirm = binding.editConfirmPassword.text.toString().trim()

            when {
                current.isEmpty() || newPassword.isEmpty() || confirm.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirm -> {
                    Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
                }
                newPassword.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
