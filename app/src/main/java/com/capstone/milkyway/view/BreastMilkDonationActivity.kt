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
import com.capstone.milkyway.databinding.ActivityBreastMilkDonationBinding
import com.capstone.milkyway.getAddressName
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class BreastMilkDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastMilkDonationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastMilkDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        supportActionBar?.hide()

        binding.locationButton.setOnClickListener { getMyLocation() }

        setupAction()
        dropDown()
    }

    private fun setupAction() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString()
            val religion = binding.autoCompleteTextView.text.toString()
            val location = binding.locationTextView.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditText.error = "Nama harus diisi"
                }
                age.isEmpty() -> {
                    binding.ageEditText.error = "Umur harus diisi"
                }
                religion.isEmpty() -> {
                    Toast.makeText(this, "Agama harus diisi", Toast.LENGTH_SHORT).show()
                }
                location.isEmpty() -> {
                    Toast.makeText(this, "Lokasi harus diisi", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
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
                val address =
                    getAddressName(this@BreastMilkDonationActivity, it.latitude, it.longitude)
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
                Toast.makeText(
                    this@BreastMilkDonationActivity,
                    religions[position],
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}