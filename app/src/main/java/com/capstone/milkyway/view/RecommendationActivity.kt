package com.capstone.milkyway.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.milkyway.UserPreference
import com.capstone.milkyway.adapter.RecommendAdapter
import com.capstone.milkyway.databinding.ActivityRecommendationBinding
import com.capstone.milkyway.loading
import com.capstone.milkyway.response.ResponseRecommendItem
import com.capstone.milkyway.viewmodel.RecommendationViewModel

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val viewModel: RecommendationViewModel by viewModels()
    private lateinit var pref: UserPreference
    private lateinit var adapter: RecommendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecommend.layoutManager = layoutManager

        pref = UserPreference(this)

        getAllRecommends()
    }

    private fun getAllRecommends() {

        val ageIntent = intent.getIntExtra(AGE, 0).toString()
        val religionIntent = intent.getStringExtra(RELIGION) ?: ""
        val bloodTypeIntent = intent.getStringExtra(BLOOD) ?: ""
        val dietaryIntent = intent.getStringExtra(DIETARY) ?: ""
        val healthIntent = intent.getStringExtra(HEALTH) ?: ""
        val isSmokingIntent = intent.getStringExtra(SMOKING) ?: ""
        val locationIntent = intent.getStringExtra(LOCATION) ?: ""

        if (ageIntent.isNotEmpty()
            && religionIntent.isNotEmpty()
            && bloodTypeIntent.isNotEmpty()
            && dietaryIntent.isNotEmpty()
            && healthIntent.isNotEmpty()
            && isSmokingIntent.isNotEmpty()
            && locationIntent.isNotEmpty()
        ) {
            if (pref.getIdToken() != "") {
                viewModel.getAllRecommend(
                    pref.getIdToken(),
                    age = 20,
                    dietary = dietaryIntent,
                    religion = religionIntent,
                    health = healthIntent,
                    smoking = isSmokingIntent,
                    blood = bloodTypeIntent,
                    location = locationIntent
                )
                viewModel.error.observe(this@RecommendationActivity) { error ->
                    viewModel.recommends.observe(this) { recommends ->
                        if (!error && recommends.isNotEmpty()) {
                            adapter = RecommendAdapter(recommends)
                            binding.rvRecommend.adapter = adapter
                            adapter.setOnItemClickCallbackRoute(object :
                                RecommendAdapter.OnItemClickCallbackRoute {
                                override fun onItemClicked(recommend: ResponseRecommendItem) {
                                    val intent =
                                        Intent(
                                            this@RecommendationActivity,
                                            RouteActivity::class.java
                                        )
                                    intent.putExtra(RouteActivity.LOCATION, recommend.location)
                                    startActivity(intent)
                                }
                            })
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) {
            loading(it, binding.progressBar)
        }
    }

    companion object {
        const val AGE = "age"
        const val RELIGION = "religion"
        const val HEALTH = "health"
        const val SMOKING = "smoking"
        const val BLOOD = "blood"
        const val DIETARY = "dietary"
        const val LOCATION = "location"
    }
}