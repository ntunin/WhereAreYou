package com.ntunin.cybervision.activity;

/**
 * Created by nik on 21.06.17.
 */

public class CRVUtils {
    private static final int Re = 6378137;

    public static void offsetFromLatLng(double[] offset, double lat0, double lng0, double lat1, double lng1) {
        offset[0] = (lat1 - lat0) * Math.PI * Re / 180;
        offset[1] = (lng1 - lng0) * Math.PI * Re * Math.cos(Math.PI * lat0 / 180) / 180;
    }
}
