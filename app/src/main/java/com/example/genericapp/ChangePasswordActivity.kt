package com.example.genericapp
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changepassword)

        findViewById<Button>(R.id.button_save_password).setOnClickListener {
            finish()
        }
    }
}