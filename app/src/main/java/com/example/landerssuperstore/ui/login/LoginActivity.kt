package com.example.landerssuperstore.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityLoginBinding
import com.example.landerssuperstore.ui.MainActivity
import com.example.landerssuperstore.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textClose.setOnClickListener { finish() }

        binding.tabSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val name = email.substringBefore("@")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    intent.putExtra("USER_NAME", name)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        binding.textForgotPassword.setOnClickListener {
            Toast.makeText(this, "Contact support to reset your password", Toast.LENGTH_SHORT).show()
        }

        binding.buttonFacebook.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_EMAIL", "facebook_user@landers.ph")
            intent.putExtra("USER_NAME", "Facebook User")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.buttonGoogle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_EMAIL", "google_user@landers.ph")
            intent.putExtra("USER_NAME", "Google User")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
