package com.capstone.milkyway

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.auth.LoginActivity
import com.capstone.milkyway.databinding.ActivityMainBinding
import com.capstone.milkyway.view.BabyCareActivity
import com.capstone.milkyway.view.BreastMilkDonationListActivity
import com.capstone.milkyway.view.BreastMilkRequestActivity
import com.capstone.milkyway.view.UserActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        pref = UserPreference(this@MainActivity)

        moveActivity()

    }

    private fun moveActivity() {
        binding.apply {
            needButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, BreastMilkRequestActivity::class.java))
            }
            donationButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, BreastMilkDonationListActivity::class.java))
            }
            careButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, BabyCareActivity::class.java))
            }
            icProfile.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (pref.getIdToken().isEmpty()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
    }
}