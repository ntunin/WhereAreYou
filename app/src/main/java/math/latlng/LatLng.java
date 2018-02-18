package math.latlng;

import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;

/**
 * Created by nik on 21.06.17.
 */

public class LatLng implements Injectable {
    public double lat;
    public double lng;
    private static final int Re = 6378137;

    @Override
    public void init(ResMap<String, Object> data) {
        ResMap<String, Object> argument = (ResMap<String, Object>) data.get("argument");
        lat = (double) argument.get("lat");
        lng = (double) argument.get("lng");
    }


    public void offsetFromLatLng(double[] offset, double lat1, double lng1) {
        offset[0] = (lat1 - lat) * Math.PI * Re / 180;
        offset[1] = (lng1 - lng) * Math.PI * Re * Math.cos(Math.PI * lat / 180) / 180;
    }
}
