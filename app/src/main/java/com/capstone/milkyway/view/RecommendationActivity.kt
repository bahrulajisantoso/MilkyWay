package com.capstone.milkyway.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
        if (pref.getIdToken() != "") {
            viewModel.getAllRecommend(
                pref.getIdToken(),
                age = 20,
                dietary = "vegan",
                religion = "islam",
                health = "ya",
                smoking = "ya",
                blood = "A",
                location = "Gambir, Jakarta"
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
                                    Intent(this@RecommendationActivity, RouteActivity::class.java)
                                intent.putExtra(RouteActivity.LOCATION, recommend.location)
                                startActivity(intent)
                            }
                        })
                    } else {
                        viewModel.message.observe(this) { message ->
                            Toast.makeText(this@RecommendationActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) {
            loading(it, binding.progressBar)
        }
    }
}