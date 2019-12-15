package com.example.carservice.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carservice.*
import com.example.carservice.item.Placemark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private fun layoutId() = R.layout.fragment_map

    private lateinit var googleMap: GoogleMap

    lateinit var mainViewModel: MainViewModel
    private val locationHelper = LocationHelper()
    lateinit var clusterManager: ClusterManager<MarkerClusterItem>
    lateinit var clusterRenderer: MarkerClusterRenderer<MarkerClusterItem>
    lateinit var clusterItemsBackup: MutableCollection<MarkerClusterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            mainViewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)
            mainViewModel.carList.observe(this, Observer { placeMarksAvailable() })
        }
    }

    private fun placeMarksAvailable() {
        map_view.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            layoutId(),
            container,
            false
        )

        locationHelper.onLocationSuccess = {
            googleMap.isMyLocationEnabled = true
        }

        locationHelper.onLocationFailure = {
            Toast.makeText(
                context,
                getString(R.string.settings_location_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_view.onCreate(savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        clusterManager = ClusterManager(context, this.googleMap)
        this.googleMap.setOnMarkerClickListener(clusterManager)
        this.googleMap.setOnCameraIdleListener(clusterManager)
        addAllMarkers()
        locationHelper.getLastLocation(this)

        clusterRenderer.setOnClusterItemClickListener { markerClusterItem ->
            val marker = clusterRenderer.getMarker(markerClusterItem)
            if (clusterManager.algorithm.items.size > 1) {
                marker.showInfoWindow()
                clusterManager.algorithm.items.let { list ->
                    list.retainAll(listOf(markerClusterItem))
                    clusterManager.clearItems()
                    clusterManager.addItems(list)
                }
            } else {
                marker.hideInfoWindow()
                clusterManager.clearItems()
                clusterManager.addItems(clusterItemsBackup)
            }
            clusterManager.cluster()
            true
        }

        clusterRenderer.setOnClusterClickListener { true }
    }

    private fun addAllMarkers() {
        mainViewModel.carList.value?.let { list ->
            list.map {
                mapCarListToMarkerOptions(it).let { option ->
                    clusterManager.addItem(
                        MarkerClusterItem(
                            option.position,
                            option.title
                        )
                    )
                }
            }
            clusterItemsBackup = clusterManager.algorithm.items
            clusterManager.cluster()
            setRenderer()
            setInitialCameraView()
        }
    }

    private fun setRenderer() {
        context?.let { context ->
            clusterManager.let {
                clusterRenderer =
                    MarkerClusterRenderer(context, googleMap, it)
                it.renderer = clusterRenderer
            }
        }
    }

    private fun mapCarListToMarkerOptions(it: Placemark): MarkerOptions {
        return MarkerOptions()
            .position(LatLng(it.coordinates[1], it.coordinates[0]))
            .title(it.name)
    }


    private fun setInitialCameraView() {
        clusterManager.algorithm.items.let {
            LatLngBounds.builder().let { builder ->
                builder.include(it.first().position).include(it.last().position)
                builder.build().let { bounds ->
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                bounds.center.latitude,
                                bounds.center.longitude
                            ), 10f
                        )
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LocationHelper.REQUEST_PERMISSION) {
            locationHelper.onPermissionsResult(this, grantResults)
        }
    }
}