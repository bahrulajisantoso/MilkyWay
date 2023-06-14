package com.capstone.milkyway.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.milkyway.databinding.ItemRowDonorBinding
import com.capstone.milkyway.response.PayloadItem

class DonorAdapter : RecyclerView.Adapter<DonorAdapter.ListViewHolder>() {

    private val listDonor = ArrayList<PayloadItem>()

    fun setListDonors(listDonor: List<PayloadItem>) {
        val diffCallback = DonorDiffCallback(this.listDonor, listDonor)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listDonor.clear()
        this.listDonor.addAll(listDonor)
        diffResult.dispatchUpdatesTo(this)
    }

    private lateinit var onItemClickCallbackEdit: OnItemClickCallbackEdit
    private lateinit var onItemClickCallbackDelete: OnItemClickCallbackDelete

    fun setOnItemClickCallbackEdit(onItemClickCallback: OnItemClickCallbackEdit) {
        this.onItemClickCallbackEdit = onItemClickCallback
    }

    fun setOnItemClickCallbackDelete(onItemClickCallback: OnItemClickCallbackDelete) {
        this.onItemClickCallbackDelete = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowDonorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val donors = listDonor[position]
        holder.bind(donors)

        holder.binding.edit.setOnClickListener {
            onItemClickCallbackEdit.onItemClicked(donors)
        }
        holder.binding.delete.setOnClickListener {
            onItemClickCallbackDelete.onItemClicked(donors)
        }
    }

    override fun getItemCount(): Int = listDonor.size


    class ListViewHolder(val binding: ItemRowDonorBinding) :
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

    interface OnItemClickCallbackEdit {
        fun onItemClicked(listDonor: PayloadItem)
    }

    interface OnItemClickCallbackDelete {
        fun onItemClicked(listDonor: PayloadItem)
    }
}