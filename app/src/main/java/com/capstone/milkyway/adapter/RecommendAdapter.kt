package com.capstone.milkyway.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.milkyway.databinding.ItemRowRecommendBinding
import com.capstone.milkyway.response.ResponseRecommendItem

class RecommendAdapter(private val listRecommend: List<ResponseRecommendItem>) :
    RecyclerView.Adapter<RecommendAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallbackRoute: OnItemClickCallbackRoute

    fun setOnItemClickCallbackRoute(onItemClickCallback: OnItemClickCallbackRoute) {
        this.onItemClickCallbackRoute = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val recommends = listRecommend[position]
        holder.bind(recommends)

        holder.binding.route.setOnClickListener {
            onItemClickCallbackRoute.onItemClicked(recommends)
        }
    }

    override fun getItemCount(): Int = listRecommend.size


    class ListViewHolder(val binding: ItemRowRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recommend: ResponseRecommendItem) {
            binding.apply {
                ageTextView.text = recommend.age.toString()
                religionTextView.text = religion(recommend.religion)
                healthTextView.text = yesOrNo(recommend.healthCondition)
                smokingTextView.text = yesOrNo(recommend.isSmoke)
                bloodTextView.text = bloodType(recommend.bloodType)
                dietaryTextView.text = dietary(recommend.dietaryRestrictions)
                locationTextView.text = recommend.location
            }
        }

        private fun yesOrNo(num: Int): String {
            return when (num) {
                0 -> "Tidak"
                1 -> "YA"
                else -> "Unknown"
            }
        }

        private fun religion(num: Int): String {
            return when (num) {
                0 -> "Islam"
                1 -> "Kristen"
                2 -> "Katholik"
                3 -> "Hindhu"
                4 -> "Budha"
                5 -> "Khonghucu"
                else -> "Unknown"
            }
        }

        private fun dietary(num: Int): String {
            return when (num) {
                0 -> "Gluten-free"
                1 -> "Lactose-free"
                2 -> "Nut-free"
                3 -> "Vegan"
                4 -> "Egg-free"
                else -> "Unknown"
            }
        }

        private fun bloodType(num: Int): String {
            return when (num) {
                0 -> "A"
                1 -> "B"
                2 -> "O"
                3 -> "AB"
                else -> "Unknown"
            }
        }
    }

    interface OnItemClickCallbackRoute {
        fun onItemClicked(recommend: ResponseRecommendItem)
    }
}

