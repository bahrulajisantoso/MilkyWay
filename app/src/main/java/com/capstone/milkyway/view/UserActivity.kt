package com.capstone.milkyway.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.MainActivity
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

            AlertDialog.Builder(this@UserActivity).apply {
                setTitle("Logout")
                setMessage("Anda yakin ingin logout?")
                setPositiveButton("Ya") { _, _ ->
                    auth.signOut()
                    pref.logOut()
                    Toast.makeText(this@UserActivity, "Logout sukses", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UserActivity, LoginActivity::class.java))
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                }
                setNegativeButton("Tidak") { _, _ -> }
                create()
                show()
            }
        }
    }
}