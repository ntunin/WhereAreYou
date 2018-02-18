package whereareyou.ntunin.com.whereareyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.ntunin.cybervision.activity.MyGameScreen;
import com.ntunin.cybervision.android.io.CRVHardwareLocationListener;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvcontext.CRVScreen;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nik on 18.02.2018.
 */

public class MapTrackingView extends CRVContext  implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FriendsTracker tracker;
    private CRVHardwareLocationListener gps;
    private Map<Friend, MarkerOptions> markers = new HashMap<>();
    MarkerOptions me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_tracking_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton arButton = (FloatingActionButton)findViewById(R.id.vr);
        arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAR();
            }
        });

    }

    @Override
    protected CRVScreen getScreen() {
        return null;
    }

    private void openAR() {
        Intent intent = new Intent(this, ARActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(18.0f);
        tracker = new FriendsTracker(this);
    }

    public void onLocationChanged(LatLng latLng, Friend friend) {
        if(me == null) {
            me = new MarkerOptions().position(latLng).title("Me");
            mMap.addMarker(me);
        }
        MarkerOptions friendMarkerOptions = markers.get(friend);
        if(friendMarkerOptions == null) {
            LatLng friendLatLng = new LatLng(friend.getLat(), friend.getLng());
            friendMarkerOptions = new MarkerOptions().position(friendLatLng).title(friend.getName());
            markers.put(friend, friendMarkerOptions);
            mMap.addMarker(friendMarkerOptions);
        }
        LatLng friendLatLng = new LatLng(friend.getLat(), friend.getLng());
        friendMarkerOptions.position(friendLatLng);
        me.position(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
