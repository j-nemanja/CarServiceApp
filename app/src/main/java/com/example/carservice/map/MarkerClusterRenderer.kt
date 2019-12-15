package com.example.carservice.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class MarkerClusterRenderer<T : ClusterItem>(
    context: Context,
    googleMap: GoogleMap,
    clusterManager: ClusterManager<T>
) : DefaultClusterRenderer<T>(context, googleMap, clusterManager) {

    override fun getMarker(clusterItem: T): Marker {
        return super.getMarker(clusterItem)
    }

}