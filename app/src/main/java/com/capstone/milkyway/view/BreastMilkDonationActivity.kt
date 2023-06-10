package com.capstone.milkyway.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.capstone.milkyway.R
import com.capstone.milkyway.UserPreference
import com.capstone.milkyway.databinding.ActivityBreastMilkDonationBinding
import com.capstone.milkyway.getAddressName
import com.capstone.milkyway.viewmodel.DonationListViewModel
import com.capstone.milkyway.viewmodel.DonationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class BreastMilkDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastMilkDonationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: DonationViewModel by viewModels()
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastMilkDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        supportActionBar?.hide()
        pref = UserPreference(this@BreastMilkDonationActivity)

        binding.locationButton.setOnClickListener { getMyLocation() }

        setupAction()
        dropdownMenu()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val nameText = binding.nameEditText.text.toString()
            val ageText = binding.ageEditText.text.toString()
            val religionText = binding.religion.text.toString()
            val phoneText = binding.phoneEditText.toString()
            val bloodTypeText = binding.blood.text.toString()
            val dietaryText = binding.dietary.text.toString()
            val healthText = binding.health.text.toString()
            val isSmokingText = binding.smoking.text.toString()
            val locationText = binding.locationEditText.toString()
            when {
                nameText.isEmpty() -> {
                    binding.nameEditText.error = "Nama harus diisi"
                }
                ageText.isEmpty() -> {
                    binding.ageEditText.error = "Umur harus diisi"
                }
                religionText.isEmpty() -> {
                    Toast.makeText(this, "Agama harus diisi", Toast.LENGTH_SHORT).show()
                }
                phoneText.isEmpty() -> {
                    binding.phoneEditText.error = "Telephone harus diisi"
                }
                bloodTypeText.isEmpty() -> {
                    Toast.makeText(this, "Gol Darah harus diisi", Toast.LENGTH_SHORT).show()
                }
                dietaryText.isEmpty() -> {
                    Toast.makeText(this, "Dietary harus diisi", Toast.LENGTH_SHORT).show()
                }
                healthText.isEmpty() -> {
                    Toast.makeText(this, "Sehat harus diisi", Toast.LENGTH_SHORT).show()
                }
                isSmokingText.isEmpty() -> {
                    Toast.makeText(this, "Merokok harus diisi", Toast.LENGTH_SHORT).show()
                }
                locationText.isEmpty() -> {
                    binding.locationEditText.error = "Lokasi harus diisi"
                }
                else -> {
                    val name = nameText.toRequestBody("text/plain".toMediaType())
                    val age = ageText.toRequestBody("text/plain".toMediaType())
                    val phone = phoneText.toRequestBody("text/plain".toMediaType())
                    val religion = religionText.toRequestBody("text/plain".toMediaType())
                    val healthCondition = healthText.toRequestBody("text/plain".toMediaType())
                    val isSmoking = isSmokingText.toRequestBody("text/plain".toMediaType())
                    val bloodType = bloodTypeText.toRequestBody("text/plain".toMediaType())
                    val dietary = dietaryText.toRequestBody("text/plain".toMediaType())
                    val location = locationText.toRequestBody("text/plain".toMediaType())
                    val userId = pref.getUserId().toRequestBody("text/plain".toMediaType())
                    val role = getString(R.string.donor).toRequestBody("text/plain".toMediaType())

                    addDonation(
                        userId = pref.getUserId(),
                        name = nameText,
                        age = ageText,
                        phone = phoneText,
                        religion = religionText,
                        healthCondition = healthText,
                        isSmoking = isSmokingText,
                        bloodType = bloodTypeText,
                        dietary = dietaryText,
                        address = locationText,
                        role = "Donor"
                    )
                }
            }
        }
    }

    private fun addDonation(
        userId: String,
        name: String,
        age: String,
        phone: String,
        religion: String,
        healthCondition: String,
        isSmoking: String,
        bloodType: String,
        dietary: String,
        address: String,
        role: String,
    ) {
        viewModel.addDonor(
            pref.getIdToken(),
            userId = userId,
            name = name,
            age = age,
            phone = phone,
            religion = religion,
            healthCondition = healthCondition,
            isSmoking = isSmoking,
            bloodType = bloodType,
            dietary = dietary,
            address = address,
            role = role
        )
        viewModel.error.observe(this) { error ->
            viewModel.message.observe(this) { message ->
                if (!error) {
                    val intent =
                        Intent(this@BreastMilkDonationActivity, DonationListViewModel::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@BreastMilkDonationActivity,
                        "Gagal",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    this@BreastMilkDonationActivity,
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