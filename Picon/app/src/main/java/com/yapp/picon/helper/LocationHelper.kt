package com.yapp.picon.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class LocationHelper(private val context: Context) {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var location: Location? = null

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun locationPermissionResult(): Location? {
        if (checkPermission()) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        }
        Toast.makeText(context, "권한이 거절되어 현재 위치 찾기가 불가합니다.", Toast.LENGTH_SHORT).show()
        return null
    }

    fun requestLocationPermissions(): Location? {
        if (checkPermission()) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        }
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), RequestCodeSet.LOCATION_REQUEST_CODE.code
        )
        return location
    }
}