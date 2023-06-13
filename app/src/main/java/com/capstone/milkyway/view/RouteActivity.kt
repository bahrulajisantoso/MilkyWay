package com.capstone.milkyway.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.capstone.milkyway.R
import com.capstone.milkyway.databinding.ActivityRouteBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject


class RouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityRouteBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var originLatitude: Double = 0.0
    private var originLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val apiKey = getString(R.string.API_KEY_DIRECTIONS)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnStart.setOnClickListener {
            mapFragment.getMapAsync {
                val originLocation = LatLng(originLatitude, originLongitude)
                googleMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = getLatLngFromAddress("Stasiun jember")
                googleMap.addMarker(MarkerOptions().position(destinationLocation!!))
                val url = getDirectionURL(originLocation, destinationLocation, apiKey)
                getDirection(url)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 16F))
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        getMyLastLocation()
    }

    private fun getLatLngFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this@RouteActivity)
        val addressList: List<Address>?
        return try {
            addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null) {
                val singleAddress: Address = addressList[0]
                LatLng(singleAddress.latitude, singleAddress.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    Toast.makeText(this, "No location access granted", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                    originLatitude = location.latitude
                    originLongitude = location.longitude
                } else {
                    Toast.makeText(
                        this@RouteActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("Start point")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 18f))
    }

    private fun getDirection(urlDirections: String) {
        val path: MutableList<List<LatLng>> = ArrayList()
        val directionsRequest =
            object : StringRequest(Method.GET, urlDirections, Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                // Get routes
                val routes = jsonResponse.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    this.googleMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.GREEN))
                }
            }, Response.ErrorListener {
            }) {}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }
}