package me.android.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import java.lang.ref.WeakReference;

public class GPSLocationManager {

    private static GPSLocationManager sGPSLocationManager;
    private GPSLocationCallback mGPSLocationCallback;
    private WeakReference<Activity> mReference;
    private LocationManager mLocationManager;

    private GPSLocationManager(Activity context) {
        this.mReference = new WeakReference<>(context);
        if (mReference.get() != null) {
            mLocationManager = (LocationManager) (mReference.get().getSystemService(Context.LOCATION_SERVICE));
        }
    }

    public static GPSLocationManager getInstance(Activity context) {
        if (sGPSLocationManager == null) {
            synchronized (GPSLocationManager.class) {
                if (sGPSLocationManager == null) {
                    sGPSLocationManager = new GPSLocationManager(context);
                }
            }
        }
        return sGPSLocationManager;
    }

    public void start(GPSLocationListener gpsLocationListener) {
        if (mReference.get() == null) {
            return;
        }
        mGPSLocationCallback = new GPSLocationCallback(gpsLocationListener);
        boolean enabled = LocationManagerCompat.isLocationEnabled(mLocationManager);
        if (!enabled) {
            openGPS();
            return;
        }
        if (ActivityCompat.checkSelfPermission(mReference.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mReference.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mGPSLocationCallback.onLocationChanged(lastKnownLocation);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, mGPSLocationCallback);
    }

    public void stop() {
        if (mReference.get() != null) {
            if (notPermission()) {
                return;
            }
            mLocationManager.removeUpdates(mGPSLocationCallback);
        }
    }

    public void openGPS() {
        Intent starter = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mReference.get().startActivityForResult(starter, 0);
    }

    public boolean notPermission() {
        return ActivityCompat.checkSelfPermission(mReference.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mReference.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }
}
