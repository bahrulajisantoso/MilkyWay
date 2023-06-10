package com.capstone.milkyway.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.milkyway.databinding.ItemRowBinding
import com.capstone.milkyway.response.PayloadItem

class ListAdapter(private val listDonor: List<PayloadItem>) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val donors = listDonor[position]
        holder.bind(donors)
    }

    override fun getItemCount(): Int = listDonor.size


    class ListViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(donor: PayloadItem) {
            binding.nameTextView.text = donor.name
            binding.ageTextView.text = donor.age.toString()
            binding.religionTextView.text = donor.religion
            binding.healthTextView.text = donor.healthCondition
            binding.smokingTextView.text = donor.isSmoke
            binding.bloodTextView.text = donor.bloodType
            binding.phoneTextView.text = donor.phone
            binding.dietaryTextView.text = donor.dietary
            binding.locationTextView.text = donor.address

        }
    }
}