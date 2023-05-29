package com.capstone.milkyway

import android.view.View
import android.widget.ProgressBar

fun showLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}


fun hideLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
}