package com.example.genericapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // ✅ Receive name and email from Login
        val name = intent.getStringExtra("USER_NAME") ?: "User"
        val email = intent.getStringExtra("USER_EMAIL") ?: "user@gmail.com"

        // ✅ Display in profile screen
        findViewById<TextView>(R.id.text_profile_name).text = name
        findViewById<TextView>(R.id.text_profile_email).text = email

        // Edit Profile
        findViewById<TextView>(R.id.text_edit_profile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Log Out → back to Login
        findViewById<LinearLayout>(R.id.button_logout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}