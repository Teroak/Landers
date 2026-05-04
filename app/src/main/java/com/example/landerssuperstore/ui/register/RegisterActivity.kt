package com.example.landerssuperstore.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityRegisterBinding
import com.example.landerssuperstore.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val firstName = binding.editFirstName.text.toString().trim()
            val lastName = binding.editLastName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val mobile = binding.editMobile.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            val confirmPassword = binding.editConfirmPassword.text.toString().trim()

            when {
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                !binding.checkboxTerms.isChecked -> {
                    Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        binding.textLogin.setOnClickListener {
            finish()
        }
    }
}
