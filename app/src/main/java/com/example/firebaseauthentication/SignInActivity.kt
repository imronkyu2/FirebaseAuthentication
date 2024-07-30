package com.example.firebaseauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebaseauthentication.databinding.ActivitySignInBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.signInActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initActionBar()
        initFirebaseAuth()


    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }


    private fun initActionBar() {
        setSupportActionBar(binding.tbSignIn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmailSignIn.text.toString().trim()
            val pass = binding.etPasswordSignIn.text.toString().trim()

            if (checkValidation(email, pass)) {
                loginToServer(email, pass)
            }
        }
        binding.btnForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.tbSignIn.setNavigationOnClickListener {
            finish()
        }

        binding.btnGoogleSignIn.setOnClickListener {
        }

        binding.btnFacebookSignIn.setOnClickListener {
        }

    }

    private fun loginToServer(email: String, pass: String) {
        val credential = EmailAuthProvider.getCredential(email, pass)
        firebaseAuth(credential)

    }

    private fun firebaseAuth(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Sing-In Failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exeption ->
                CustomDialog.hideLoading()
                Toast.makeText(this, exeption.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmailSignIn.error = "Please field your email"
            binding.etEmailSignIn.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailSignIn.error = "Please use valid email"
            binding.etEmailSignIn.requestFocus()
        } else if (pass.isEmpty()) {
            binding.etPasswordSignIn.error = "Please field your password"
            binding.etPasswordSignIn.requestFocus()
        } else {
            return true
        }
        CustomDialog.hideLoading()
        return false
    }
}