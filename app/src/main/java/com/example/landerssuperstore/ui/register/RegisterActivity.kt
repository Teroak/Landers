package com.example.landerssuperstore.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityRegisterBinding
import com.example.landerssuperstore.ui.login.LoginActivity
import com.example.landerssuperstore.utils.MembershipManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
                    registerUser(firstName, lastName, email, mobile, password)
                }
            }
        }

        binding.textLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, mobile: String, password: String) {
        binding.buttonRegister.isEnabled = false
        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserToFirestore(userId, firstName, lastName, email, mobile)
                    }
                } else {
                    binding.buttonRegister.isEnabled = true
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String, firstName: String, lastName: String, email: String, mobile: String) {
        val calendar = Calendar.getInstance()
        val joinedDate = calendar.time
        calendar.add(Calendar.YEAR, 1)
        val expiryDate = calendar.time

        // Check if the user should be an admin based on email
        if (email.lowercase() == "admin@landers.ph") {
            val employeeMap = hashMapOf(
                "Emp_ID" to userId,
                "Emp_FirstName" to firstName,
                "Emp_LastName" to lastName,
                "Emp_Email" to email,
                "Emp_PhoneNumber" to mobile,
                "Emp_Role" to "Admin",
                "Emp_Status" to "Active",
                "Emp_DateJoined" to joinedDate
            )

            db.collection("employees").document(userId)
                .set(employeeMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Admin account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    binding.buttonRegister.isEnabled = true
                    Toast.makeText(this, "Error saving admin profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            val userMap = hashMapOf(
                "Mem_ID" to userId,
                "Mem_FirstName" to firstName,
                "Mem_LastName" to lastName,
                "Mem_Email" to email,
                "Mem_PhoneNumber" to mobile,
                "Mem_Type" to "Premium",
                "Mem_Status" to "Active",
                "Mem_DateJoined" to joinedDate,
                "Mem_ExpiryDate" to expiryDate,
                "Mem_Address" to "" // Initial empty address
            )

            db.collection("members").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    MembershipManager.setMembershipStatus(true)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    binding.buttonRegister.isEnabled = true
                    Toast.makeText(this, "Error saving profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
