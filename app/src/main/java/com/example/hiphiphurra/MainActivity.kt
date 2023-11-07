package com.example.hiphiphurra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference UI components
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Simple validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Placeholder login logic
            if (username == "admin" && password == "password") {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity or perform another action here if required
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

