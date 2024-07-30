package com.example.firebaseauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebaseauthentication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.signUpActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.btnSignUp.setOnClickListener {
            val email =   binding.etEmailSignUp.text.toString().trim()
            val pass =   binding.etPasswordSignUp.text.toString().trim()
            val confirmPass =   binding.etConfirmPasswordSignUp.text.toString().trim()

            CustomDialog.showLoading(this)
            if (checkValidation(email, pass, confirmPass)){
                registerToServer(email, pass)
            }
        }

        binding.tbSignUp.setNavigationOnClickListener {
            finish()
        }
    }

    private fun checkValidation(email: String, pass: String, confirmPass: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmailSignUp.error = "Silakan isi email Anda"
            binding.etEmailSignUp.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailSignUp.error = "Silakan gunakan email yang valid"
            binding.etEmailSignUp.requestFocus()
        } else if (pass.isEmpty()) {
            binding.etPasswordSignUp.error = "Silakan isi password Anda"
            binding.etPasswordSignUp.requestFocus()
        } else if (confirmPass.isEmpty()) {
            binding.etConfirmPasswordSignUp.error = "Silakan isi konfirmasi password Anda"
            binding.etConfirmPasswordSignUp.requestFocus()
        } else if (pass != confirmPass) {
            binding.etPasswordSignUp.error = "Password Anda tidak cocok"
            binding.etConfirmPasswordSignUp.error = "Konfirmasi password Anda tidak cocok"

            binding.etPasswordSignUp.requestFocus()
            binding.etConfirmPasswordSignUp.requestFocus()
        } else {
            binding.etPasswordSignUp.error = null
            binding.etConfirmPasswordSignUp.error = null
            return true
        }
        CustomDialog.hideLoading()
        return false
    }


    private fun registerToServer(email: String, pass: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                CustomDialog.hideLoading()
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
    }


}