package com.example.firebaseauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebaseauthentication.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ForgotPasswordActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSendEmail.setOnClickListener {
            val email = binding.etEmailForgotPass.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmailForgotPass.error = "Please field your email"
                binding.etEmailForgotPass.requestFocus()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailForgotPass.error = "Please use valid email"
                binding.etEmailForgotPass.requestFocus()
                return@setOnClickListener
            } else {
                forgotPassword(email)
            }
        }
    }

    private fun forgotPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Your reset email has been sent to your email",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Faild reset password", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exeption ->
                Toast.makeText(this, exeption.message, Toast.LENGTH_SHORT).show()
            }
    }

}