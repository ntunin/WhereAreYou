package com.ntunin.cybervision.android.io;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvcontext.PermissionListener;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.res.ResMap;

import java.util.List;

/**
 * Created by nik on 16.06.17.
 */

public class CRVHardwareLocationListener implements LocationListener, Injectable {
    Location current;
    int minTime;
    int minDistance;
    private LocationManager manager;

    public Location getCurrentLocation() {
        return current;
    }

    @Override
    public void onLocationChanged(Location location) {
        current = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void init(ResMap<String, Object> data) {
        Object tmp;
        tmp = data.get("gpsMinTime");
        minTime = (tmp != null)? (int)tmp : 5000;
        tmp = data.get("gpsMinDistance");
        minDistance = (tmp != null)? (int) tmp: 10;
        Context context = CRVContext.current();
        if(isPermissionGranted()) {
            onStartResolve();
        } else {
            requestPermissions();
        }
    }


    private boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(CRVContext.current(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        CRVContext.sendGrantRequest(
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                R.string.gps_permission,
                new PermissionListener() {
                    @Override
                    public void onPermissionResult(int[] results) {
                        if(results[0] == PackageManager.PERMISSION_GRANTED) {
                            onStartResolve();
                        } else {
                            ERRNO.write(R.string.camera_not_granted);
                        }
                    }
                }
        );

    }

    @SuppressWarnings({"MissingPermission"})
    private void  onStartResolve() {
        manager = (LocationManager)
                CRVContext.current().getSystemService(Context.LOCATION_SERVICE);
        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, minTime, minDistance,
                    this);
        } catch (Exception e) {
            Log.d("GPS", e.getMessage());
            return;
        }
        List<String> providers = manager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = manager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        current = bestLocation;

    }

}
