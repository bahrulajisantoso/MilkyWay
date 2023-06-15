package com.capstone.milkyway.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.milkyway.R
import com.capstone.milkyway.UserPreference
import com.capstone.milkyway.auth.LoginActivity
import com.capstone.milkyway.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val auth = FirebaseAuth.getInstance()
        val pref = UserPreference(this)

        val id = pref.getUserId()
        val email = pref.getEmail()

        binding.idTextView.text = id
        binding.emailTextView.text = email

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            pref.logOut()
            startActivity(Intent(this@UserActivity, LoginActivity::class.java))
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
    }
}