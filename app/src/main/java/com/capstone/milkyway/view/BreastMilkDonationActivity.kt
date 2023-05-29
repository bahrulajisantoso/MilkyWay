package com.capstone.milkyway.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.databinding.ActivityBreastMilkDonationBinding

class BreastMilkDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastMilkDonationBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastMilkDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}