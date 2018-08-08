package com.tcv.peliculas.controller.Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationViewModel {

    private LocationHelper locationHelper;
    private Context context;

    public LocationViewModel(Context context) {
        this.context = context;
        this.locationHelper = new LocationHelper(context);
    }

    public String getCityName() {
        Location location = locationHelper.getLastKnownLocation();
        if (location == null){
            return "Buenos Aires";
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        Address address = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                address = addresses.get(0);
            return address.getLocality() + " ," + address.getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
