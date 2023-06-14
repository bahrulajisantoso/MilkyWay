package com.capstone.milkyway.adapter

import androidx.recyclerview.widget.DiffUtil
import com.capstone.milkyway.response.PayloadItem

class DonorDiffCallback(
    private val mOldNoteList: List<PayloadItem>,
    private val mNewNoteList: List<PayloadItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldNoteList.size
    }

    override fun getNewListSize(): Int {
        return mNewNoteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList[oldItemPosition].id == mNewNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldNoteList[oldItemPosition]
        val newEmployee = mNewNoteList[newItemPosition]
        return oldEmployee.id == newEmployee.id
                && oldEmployee.name == newEmployee.name
                && oldEmployee.age == newEmployee.age
                && oldEmployee.religion == newEmployee.religion
                && oldEmployee.healthCondition == newEmployee.healthCondition
                && oldEmployee.isSmoke == newEmployee.isSmoke
                && oldEmployee.bloodType == newEmployee.bloodType
                && oldEmployee.phone == newEmployee.phone
                && oldEmployee.address == newEmployee.address
    }
}