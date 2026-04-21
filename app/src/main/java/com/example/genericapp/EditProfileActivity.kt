package com.example.genericapp
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)

        // Save → go back to Profile
        findViewById<Button>(R.id.button_save_profile).setOnClickListener {
            finish() // goes back to ProfileActivity
        }

        // Cancel → go back
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            finish()
        }
    }
}