package com.capstone.milkyway

import android.content.ContentValues
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import java.io.IOException
import java.util.*

fun showLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}


fun hideLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
}

fun getAddressName(context: Context, lat: Double, lon: Double): String? {
    var addressName: String? = null
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val list = geocoder.getFromLocation(lat, lon, 1)
        if (list != null && list.size != 0) {
            addressName = list[0].getAddressLine(0)
            Log.d(ContentValues.TAG, "getAddressName: $addressName")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return addressName
}

fun loading(isLoading: Boolean, progressBar: ProgressBar) {
    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
}