package me.android.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

public class GPSLocationCallback implements LocationListener {

    private GPSLocationListener mGPSLocationListener;

    public GPSLocationCallback(GPSLocationListener GPSLocationListener) {
        mGPSLocationListener = GPSLocationListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mGPSLocationListener.updateLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        mGPSLocationListener.updateStatus(provider, status, extras);
        switch (status) {
            case LocationProvider.AVAILABLE:
                mGPSLocationListener.updateGPSProviderStatus(GPSProviderStatus.GPS_AVAILABLE);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                mGPSLocationListener.updateGPSProviderStatus(GPSProviderStatus.GPS_OUT_OF_SERVICE);
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                mGPSLocationListener.updateGPSProviderStatus(GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE);
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        mGPSLocationListener.updateGPSProviderStatus(GPSProviderStatus.GPS_ENABLED);
    }

    @Override
    public void onProviderDisabled(String provider) {
        mGPSLocationListener.updateGPSProviderStatus(GPSProviderStatus.GPS_DISABLED);
    }

}
