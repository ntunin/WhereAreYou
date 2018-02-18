package whereareyou.ntunin.com.whereareyou;

import android.app.VoiceInteractor;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.ntunin.cybervision.android.io.CRVHardwareLocationListener;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.res.ResMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nik on 18.02.2018.
 */

public class FriendsTracker {
    RequestQueue queue;
    String url;
    CRVHardwareLocationListener gps;
    MapTrackingView context;

    FriendsTracker(MapTrackingView context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        url = context.getString(R.string.api);
        CRVContext.setCurrent(context);
        gps = new CRVHardwareLocationListener();
        gps.init(new ResMap<String, Object>());
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                List<Friend> selectedFriends = (List<Friend>)Shared.getBundle().get("selected_friends");
                for (Friend friend:
                        selectedFriends) {
                    syncWith(friend);
                }
            }
        },0,5000);
    }

    private void syncWith(final Friend friend) {

        try {
            final Location location = gps.getCurrentLocation();
            if(location == null) {
                return;
            }
            String myId = (String)Shared.getBundle().get("device_id");
            JSONObject latLng = new JSONObject();
            latLng.put("lat", location.getLatitude());
            latLng.put("lng", location.getLongitude());
            JSONObject data = new JSONObject();
            data.put("location", latLng);
            data.put("my-id", myId);
            data.put("target-id", myId);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String status = response.getString("status");
                        if(status == "error") {
                            return;
                        }
                        double lat = response.getDouble("lat");
                        double lng = response.getDouble("lng");
                        friend.setLatLng(lat, lng);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        context.onLocationChanged(latLng, friend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.getMessage());
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
