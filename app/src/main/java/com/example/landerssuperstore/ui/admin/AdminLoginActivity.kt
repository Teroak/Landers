package com.example.landerssuperstore.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityAdminLoginBinding

import com.example.landerssuperstore.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonAdminLogin.setOnClickListener {
            val email = binding.editAdminEmail.text.toString().trim()
            val password = binding.editAdminPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter admin credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.buttonAdminLogin.isEnabled = false
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkAdminAccess(auth.currentUser?.uid ?: "")
                    } else {
                        binding.buttonAdminLogin.isEnabled = true
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkAdminAccess(userId: String) {
        db.collection("employees").document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val role = doc.getString("Emp_Role")
                    if (role == "Admin") {
                        val intent = Intent(this, AdminDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        auth.signOut()
                        binding.buttonAdminLogin.isEnabled = true
                        Toast.makeText(this, "Access denied: Not an Admin", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    auth.signOut()
                    binding.buttonAdminLogin.isEnabled = true
                    Toast.makeText(this, "Access denied: Employee record not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                auth.signOut()
                binding.buttonAdminLogin.isEnabled = true
                Toast.makeText(this, "Error checking access: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
