package me.android.location;

import android.location.Location;
import android.os.Bundle;

public interface GPSLocationListener {

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    void updateLocation(Location location);

    void updateStatus(String provider, int status, Bundle extras);

    void updateGPSProviderStatus(int gpsStatus);

}
