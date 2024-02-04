package com.nb.findfalcone.ui.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.nb.findfalcone.R

fun Activity.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    if (!this.isFinishing)
        Toast.makeText(this, msg, length).show()
}

fun Context.getVehicleImage(vehicle: String?): Int {
    return when (vehicle) {
        "Space pod" -> R.drawable.space_pod
        "Space rocket" -> R.drawable.space_rocket
        "Space shuttle" -> R.drawable.space_shuttle
        "Space ship" -> R.drawable.space_ship
        else -> R.drawable.space_ship
    }
}

fun Context.getPlanetImage(planet: String?): Int {
    return when (planet) {
        "Donlon" -> R.drawable.planet_donlon
        "Enchai" -> R.drawable.planet_enchai
        "Jebing" -> R.drawable.planet_jebing
        "Sapir" -> R.drawable.planet_sapir
        "Lerbin" -> R.drawable.planet_lerbin
        "Pingasor" -> R.drawable.planet_pingasor
        else -> R.drawable.planet_donlon
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}
