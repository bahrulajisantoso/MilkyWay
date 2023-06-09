package com.capstone.milkyway.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.milkyway.databinding.ActivityRouteBinding

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}