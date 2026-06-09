package org.example.project.data

import dev.icerock.moko.geo.LocationTracker
import org.example.project.domain.MyLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepository(private val locationTracker: LocationTracker) {

    // Converte o LatLng nativo do MOKO para o modelo do seu Domain
    fun getLocationUpdates(): Flow<MyLocation> {
        return locationTracker.getLocationsFlow().map { latLng ->
            MyLocation(
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
        }
    }

    suspend fun startTracking() {
        locationTracker.startTracking()
    }

    fun stopTracking() {
        locationTracker.stopTracking()
    }
}