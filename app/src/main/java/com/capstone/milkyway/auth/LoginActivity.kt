package com.capstone.milkyway.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.*
import com.capstone.milkyway.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        pref = UserPreference(this)

        binding.accountTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
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
                    login(email, password)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        showLoading(binding.progressBar)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    auth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (it.isSuccessful) {
                            val idToken: String = task.result?.token.toString()
                            val userId = auth.currentUser?.uid.toString()
                            val emailPref = auth.currentUser?.email.toString()

                            val pref = UserPreference(this@LoginActivity)
                            pref.setUser(userId, idToken, emailPref)

                            Toast.makeText(this, "Login sukses", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            finish()
                            hideLoading(binding.progressBar)
                        } else {
                            Log.d("Error", task.exception.toString())
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal\nPastikan email dan password benar",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideLoading(binding.progressBar)
                }
            }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f)
                .setDuration(500)
        val accountTextView =
            ObjectAnimator.ofFloat(binding.accountTextView, View.ALPHA, 1f)
                .setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailEditText,
                passwordEditText,
                accountTextView,
                login
            )
            startDelay = 500
        }.start()
    }

    override fun onStart() {
        super.onStart()
        if (pref.getIdToken().isNotEmpty()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
    }
}