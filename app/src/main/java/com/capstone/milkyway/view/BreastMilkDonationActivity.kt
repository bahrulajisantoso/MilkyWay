package com.capstone.milkyway.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.capstone.milkyway.loading
import com.capstone.milkyway.viewmodel.DonationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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

        val uuidIntent = intent.getStringExtra(UUID) ?: ""
        val nameIntent = intent.getStringExtra(NAME) ?: ""
        val ageIntent = intent.getIntExtra(AGE, 0).toString()
        val religionIntent = intent.getStringExtra(RELIGION) ?: ""
        val phoneIntent = intent.getStringExtra(PHONE) ?: ""
        val bloodTypeIntent = intent.getStringExtra(BLOOD) ?: ""
        val dietaryIntent = intent.getStringExtra(DIETARY) ?: ""
        val healthIntent = intent.getStringExtra(HEALTH) ?: ""
        val isSmokingIntent = intent.getStringExtra(SMOKING) ?: ""
        val locationIntent = intent.getStringExtra(LOCATION) ?: ""

        if (uuidIntent.isNotEmpty()
            && nameIntent.isNotEmpty()
            && ageIntent.isNotEmpty()
            && religionIntent.isNotEmpty()
            && phoneIntent.isNotEmpty()
            && bloodTypeIntent.isNotEmpty()
            && dietaryIntent.isNotEmpty()
            && healthIntent.isNotEmpty()
            && isSmokingIntent.isNotEmpty()
            && locationIntent.isNotEmpty()
        ) {
            binding.apply {
                nameEditText.setText(nameIntent)
                ageEditText.setText(ageIntent)
                religion.setText(religionIntent)
                phoneEditText.setText(phoneIntent)
                blood.setText(bloodTypeIntent)
                dietary.setText(dietaryIntent)
                health.setText(healthIntent)
                smoking.setText(isSmokingIntent)
                locationEditText.setText(locationIntent)

                ageEditText.isEnabled = false
                ageEditText.setTextColor(getColor(R.color.gray))
                religionTextInputLayout.isEnabled = false
                religion.setTextColor(getColor(R.color.gray))
                bloodTextInputLayout.isEnabled = false
                blood.setTextColor(getColor(R.color.gray))
                dietaryTextInputLayout.isEnabled = false
                dietary.setTextColor(getColor(R.color.gray))
                healthTextInputLayout.isEnabled = false
                health.setTextColor(getColor(R.color.gray))
                smokingTextInputLayout.isEnabled = false
                smoking.setTextColor(getColor(R.color.gray))
            }
        }

        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString()
            val religion = binding.religion.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val bloodType = binding.blood.text.toString()
            val dietary = binding.dietary.text.toString()
            val health = binding.health.text.toString()
            val isSmoking = binding.smoking.text.toString()
            val location = binding.locationEditText.text.toString()
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

                    if (uuidIntent.isNotEmpty()
                        && nameIntent.isNotEmpty()
                        && ageIntent.isNotEmpty()
                        && religionIntent.isNotEmpty()
                        && phoneIntent.isNotEmpty()
                        && bloodTypeIntent.isNotEmpty()
                        && dietaryIntent.isNotEmpty()
                        && healthIntent.isNotEmpty()
                        && isSmokingIntent.isNotEmpty()
                        && locationIntent.isNotEmpty()
                    ) {
                        updateDonor(
                            token = pref.getIdToken(),
                            uuid = uuidIntent,
                            name = name,
                            phone = phone.toInt(),
                            address = location,
                        )
                    } else {
                        val ageInt = age.toInt()
                        val phoneInt = age.toInt()
                        val donor = getString(R.string.donorRole)

                        addDonor(
                            token = pref.getIdToken(),
                            userId = pref.getUserId(),
                            name = name,
                            age = ageInt,
                            phone = phoneInt,
                            religion = religion,
                            healthCondition = health,
                            isSmoking = isSmoking,
                            bloodType = bloodType,
                            dietary = dietary,
                            address = location,
                            role = donor
                        )
                    }
                }
            }
        }
    }

    private fun addDonor(
        token: String,
        userId: String,
        name: String,
        age: Int,
        phone: Int,
        religion: String,
        healthCondition: String,
        isSmoking: String,
        bloodType: String,
        dietary: String,
        address: String,
        role: String,
    ) {
        viewModel.addDonor(
            token = token,
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
                    Toast.makeText(
                        this@BreastMilkDonationActivity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(
                            this@BreastMilkDonationActivity,
                            BreastMilkDonationListActivity::class.java
                        )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@BreastMilkDonationActivity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            loading(it, binding.progressBar)
        }
    }

    private fun updateDonor(
        token: String,
        uuid: String,
        name: String,
        phone: Int,
        address: String,
    ) {
        viewModel.updateDonor(
            token = token,
            uuid = uuid,
            name = name,
            phone = phone,
            address = address,
        )
        viewModel.error.observe(this) { error ->
            viewModel.message.observe(this) { message ->
                if (!error) {
                    Toast.makeText(
                        this@BreastMilkDonationActivity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(
                            this@BreastMilkDonationActivity,
                            BreastMilkDonationListActivity::class.java
                        )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@BreastMilkDonationActivity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            loading(it, binding.progressBar)
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
        const val UUID = "uuid"
        const val NAME = "name"
        const val AGE = "age"
        const val PHONE = "phone"
        const val RELIGION = "religion"
        const val HEALTH = "health"
        const val SMOKING = "smoking"
        const val BLOOD = "blood"
        const val DIETARY = "dietary"
        const val LOCATION = "location"
    }
}