package com.example.carservice.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.example.carservice.R
import com.google.android.gms.location.LocationServices

class LocationHelper {

    lateinit var onLocationSuccess: (Location) -> Unit
    lateinit var onLocationFailure: () -> Unit

    companion object {
        const val REQUEST_PERMISSION = 101
    }

    fun onPermissionsResult(
        fragment: Fragment,
        grantResults: IntArray
    ){
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getLastLocation(fragment)
        } else {
            if(::onLocationFailure.isInitialized){
                onLocationFailure()
            }
        }
    }

    private fun checkPermission(context: Context, permission: String): Boolean{
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions(context: Context): Boolean {
        return checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestPermissions(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_PERMISSION
        )
    }


    fun getLastLocation(fragment: Fragment) {
        fragment.context?.let { context ->
            if (checkPermissions(context)) {
                if (LocationManagerCompat.isLocationEnabled(
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    )) {
                    LocationServices.getFusedLocationProviderClient(
                        context
                    ).let { locationProvider ->
                        locationProvider.lastLocation.addOnSuccessListener { location: Location? ->
                            location?.let {
                                if(::onLocationSuccess.isInitialized){
                                    onLocationSuccess(it)
                                }
                            } ?: run {
                                if(::onLocationFailure.isInitialized){
                                    onLocationFailure()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.settings_location_message
                        ), Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }
            } else {
                requestPermissions(fragment)
            }
        }
    }
}