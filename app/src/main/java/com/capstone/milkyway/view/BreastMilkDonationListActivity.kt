package com.capstone.milkyway.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.milkyway.UserPreference
import com.capstone.milkyway.adapter.ListAdapter
import com.capstone.milkyway.databinding.ActivityBreastMilkDonationListBinding
import com.capstone.milkyway.loading
import com.capstone.milkyway.response.PayloadItem
import com.capstone.milkyway.viewmodel.DonationListViewModel

class BreastMilkDonationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreastMilkDonationListBinding
    private val viewModel: DonationListViewModel by viewModels()
    private lateinit var pref: UserPreference
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreastMilkDonationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvDonor.layoutManager = layoutManager

        pref = UserPreference(this@BreastMilkDonationListActivity)

        binding.fabAdd.setOnClickListener {
            startActivity(
                Intent(
                    this@BreastMilkDonationListActivity,
                    BreastMilkDonationActivity::class.java
                )
            )
        }

        getAllDonors()
    }

    private fun getAllDonors() {
        if (pref.getIdToken() != "") {
            viewModel.getAllDonors(pref.getIdToken())
            viewModel.donors.observe(this) {
                adapter = ListAdapter(it)
                binding.rvDonor.adapter = adapter

                adapter.setOnItemClickCallbackEdit(object : ListAdapter.OnItemClickCallbackEdit {
                    override fun onItemClicked(listDonor: PayloadItem) {
                        val intent = Intent(
                            this@BreastMilkDonationListActivity,
                            BreastMilkDonationActivity::class.java
                        )
                        intent.putExtra(BreastMilkDonationActivity.UUID, listDonor.uuid)
                        intent.putExtra(BreastMilkDonationActivity.NAME, listDonor.name)
                        intent.putExtra(BreastMilkDonationActivity.AGE, listDonor.age)
                        intent.putExtra(BreastMilkDonationActivity.PHONE, listDonor.phone)
                        intent.putExtra(BreastMilkDonationActivity.RELIGION, listDonor.religion)
                        intent.putExtra(BreastMilkDonationActivity.HEALTH, listDonor.healthCondition)
                        intent.putExtra(BreastMilkDonationActivity.SMOKING, listDonor.isSmoke)
                        intent.putExtra(BreastMilkDonationActivity.BLOOD, listDonor.bloodType)
                        intent.putExtra(BreastMilkDonationActivity.DIETARY, listDonor.dietary)
                        intent.putExtra(BreastMilkDonationActivity.LOCATION, listDonor.address)
                        startActivity(intent)
                    }
                })
                adapter.setOnItemClickCallbackDelete(object :
                    ListAdapter.OnItemClickCallbackDelete {
                    override fun onItemClicked(listDonor: PayloadItem) {
                        deleteDonor(listDonor.uuid)
                    }
                })
            }

            viewModel.isLoading.observe(this) {
                loading(it, binding.progressBar)
            }
        }
    }

    private fun deleteDonor(uuid: String) {
        if (pref.getIdToken() != "") {
            viewModel.donors.observe(this) {
                viewModel.deleteDonor(pref.getIdToken(), uuid)
            }
        }
    }
}