package com.capstone.milkyway.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.milkyway.R
import com.capstone.milkyway.UserPreference
import com.capstone.milkyway.viewmodel.DonationListViewModel

class BreastMilkDonationListActivity : AppCompatActivity() {

    private val viewModel by viewModels<DonationListViewModel>()
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breast_milk_donation_list)

        pref = UserPreference(this@BreastMilkDonationListActivity)

        getAllDonors()
    }

    private fun getAllDonors() {
        if (pref.getIdToken() != "")
            viewModel.getAllDonors(pref.getIdToken())
    }
}