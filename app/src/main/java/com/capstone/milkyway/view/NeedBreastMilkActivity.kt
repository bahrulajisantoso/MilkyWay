package com.capstone.milkyway.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.databinding.ActivityNeedBreastMilkBinding

class NeedBreastMilkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNeedBreastMilkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeedBreastMilkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}