package com.capstone.milkyway.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.*
import com.capstone.milkyway.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        pref = UserPreference(this)

        binding.accountTextView.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        setupAction()
        playAnimation()

    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditText.error = getString(R.string.empty_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = getString(R.string.empty_pass)
                }
                password.length < 8 -> {
                    binding.passwordEditText.error = getString(R.string.invalid_pass)
                }
                else -> {
                    register(email, password)
                }
            }
        }
    }

    private fun register(email: String, password: String) {
        showLoading(binding.progressBar)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Register sukses", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    hideLoading(binding.progressBar)
                } else {
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // email already in use
                        Toast.makeText(
                            applicationContext,
                            "Email sudah digunakan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideLoading(binding.progressBar)
                }
            }
    }

    private fun playAnimation() {

        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
                .setDuration(500)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val accountTextView =
            ObjectAnimator.ofFloat(binding.accountTextView, View.ALPHA, 1f).setDuration(500)
        val signup =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                emailEditText,
                passwordEditText,
                accountTextView,
                signup
            )
            startDelay = 500
        }.start()
    }

    override fun onStart() {
        super.onStart()
        if (pref.getIdToken().isNotEmpty()) {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
    }
}