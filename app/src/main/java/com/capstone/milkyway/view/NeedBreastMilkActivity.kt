package com.capstone.milkyway.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.capstone.milkyway.R
import com.capstone.milkyway.databinding.ActivityNeedBreastMilkBinding
import com.capstone.milkyway.getAddressName
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class NeedBreastMilkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNeedBreastMilkBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeedBreastMilkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.locationButton.setOnClickListener { getMyLocation() }

        dropDown()
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSIONS
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val address = getAddressName(this@NeedBreastMilkActivity, it.latitude, it.longitude)
                binding.currentLocTextView.text = address
            }
        }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Failed get current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun dropDown() {
        val religions = resources.getStringArray(R.array.religions)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, religions)
        val autocomplete = binding.autoCompleteTextView
        autocomplete.setAdapter(arrayAdapter)

        autocomplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Toast.makeText(this@NeedBreastMilkActivity, religions[position], Toast.LENGTH_SHORT)
                    .show()
            }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}