package com.capstone.milkyway.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.databinding.ActivityBabyCareBinding

class BabyCareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBabyCareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyCareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}