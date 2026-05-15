package com.example.landerssuperstore.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityLoginBinding
import com.example.landerssuperstore.ui.MainActivity
import com.example.landerssuperstore.ui.admin.AdminDashboardActivity
import com.example.landerssuperstore.ui.admin.AdminLoginActivity
import com.example.landerssuperstore.ui.admin.DriverDashboardActivity
import com.example.landerssuperstore.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
                    loginUser(email, password)
                }
            }
        }

        // Add long press listener for admin dashboard access
        binding.textOrLogin.setOnLongClickListener {
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
            true // Return true to indicate the event is consumed
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

    private fun loginUser(email: String, password: String) {
        binding.buttonLogin.isEnabled = false
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fetchUserProfileAndNavigate()
                } else {
                    binding.buttonLogin.isEnabled = true
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun fetchUserProfileAndNavigate() {
        val userId = auth.currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        
        // First check if user is an employee
        db.collection("employees").document(userId).get()
            .addOnSuccessListener { employeeDoc ->
                if (employeeDoc != null && employeeDoc.exists()) {
                    val role = employeeDoc.getString("Emp_Role")
                    val name = employeeDoc.getString("Emp_FirstName") ?: "Staff"
                    val email = employeeDoc.getString("Emp_Email") ?: ""

                    when (role) {
                        "Admin" -> {
                            val intent = Intent(this, AdminDashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        "Delivery" -> {
                            val intent = Intent(this, DriverDashboardActivity::class.java)
                            intent.putExtra("USER_NAME", name)
                            intent.putExtra("USER_EMAIL", email)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            // Default Staff or other roles to Admin dashboard for now
                            val intent = Intent(this, AdminDashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    // Not an employee, check if member
                    db.collection("members").document(userId).get()
                        .addOnSuccessListener { memberDoc ->
                            if (memberDoc != null && memberDoc.exists()) {
                                val firstName = memberDoc.getString("Mem_FirstName") ?: "User"
                                val email = memberDoc.getString("Mem_Email") ?: "user@landers.ph"
                                
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("USER_EMAIL", email)
                                intent.putExtra("USER_NAME", firstName)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                // Fallback to MainActivity
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                }
            }
            .addOnFailureListener {
                // Navigate anyway on failure if authenticated
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
    }
}
