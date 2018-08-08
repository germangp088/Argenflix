package com.tcv.peliculas.controller.Location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class LocationHelper {

    private Context context;

    public LocationHelper(Context context) {
        this.context = context;
    }

    public Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager)
                context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
