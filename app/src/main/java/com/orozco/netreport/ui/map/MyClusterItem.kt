package com.orozco.netreport.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.orozco.netreport.model.LocationPoint

class MyClusterItem @JvmOverloads constructor(lat: Double, lng: Double, val locationPoint: LocationPoint) : ClusterItem {
    private val position: LatLng = LatLng(lat, lng)
    override fun getPosition(): LatLng = position
    override fun getTitle(): String = locationPoint.label
    override fun getSnippet(): String = "speed: ${locationPoint.bandwidth}\nsignal: ${locationPoint.signal}"
}