package com.capstone.milkyway.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.capstone.milkyway.R
import com.capstone.milkyway.databinding.ActivityBreastMilkRequestBinding
import com.capstone.milkyway.getAddressName
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class BreastMilkRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastMilkRequestBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastMilkRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.locationButton.setOnClickListener { getMyLocation() }

        setupAction()
        dropdownMenu()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString()
            val religion = binding.religion.text.toString()
            val phone = binding.phoneEditText.toString()
            val bloodType = binding.blood.toString()
            val dietary = binding.dietary.toString()
            val health = binding.health.toString()
            val isSmoking = binding.smoking.toString()
            val location = binding.locationEditText.toString()
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
                phone.isEmpty() -> {
                    binding.phoneEditText.error = "Telephone harus diisi"
                }
                bloodType.isEmpty() -> {
                    Toast.makeText(this, "Gol Darah harus diisi", Toast.LENGTH_SHORT).show()
                }
                dietary.isEmpty() -> {
                    Toast.makeText(this, "Dietary harus diisi", Toast.LENGTH_SHORT).show()
                }
                health.isEmpty() -> {
                    Toast.makeText(this, "Sehat harus diisi", Toast.LENGTH_SHORT).show()
                }
                isSmoking.isEmpty() -> {
                    Toast.makeText(this, "Merokok harus diisi", Toast.LENGTH_SHORT).show()
                }
                location.isEmpty() -> {
                    binding.locationEditText.error = "Lokasi harus diisi"
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
                    getAddressName(this@BreastMilkRequestActivity, it.latitude, it.longitude)
                binding.locationEditText.setText(address)
            }
        }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Failed get current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setDropdown(array: Array<String>, autoCompleteTextView: AutoCompleteTextView) {
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, array)
        autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Toast.makeText(
                    this@BreastMilkRequestActivity,
                    array[position],
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    private fun dropdownMenu() {
        binding.apply {
            setDropdown(resources.getStringArray(R.array.religions), religion)
            setDropdown(resources.getStringArray(R.array.bloodTypes), blood)
            setDropdown(resources.getStringArray(R.array.dietaries), dietary)
            setDropdown(resources.getStringArray(R.array.healths), health)
            setDropdown(resources.getStringArray(R.array.isSmokings), smoking)
        }

    }


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}