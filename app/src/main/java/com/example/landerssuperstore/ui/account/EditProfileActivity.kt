package com.example.landerssuperstore.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentName = intent.getStringExtra("CURRENT_NAME") ?: ""
        val currentEmail = intent.getStringExtra("CURRENT_EMAIL") ?: ""

        binding.editName.setText(currentName)
        binding.editEmail.setText(currentEmail)

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonSave.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val result = Intent()
                result.putExtra("NEW_NAME", name)
                result.putExtra("NEW_EMAIL", email)
                setResult(Activity.RESULT_OK, result)
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
