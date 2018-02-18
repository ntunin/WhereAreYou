package whereareyou.ntunin.com.whereareyou;

/**
 * Created by nik on 18.02.2018.
 */


import android.location.Location;
import android.util.Log;

import com.ntunin.cybervision.activity.*;
import com.ntunin.cybervision.android.io.CRVHardwareLocationListener;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.crvview.screen.CRVHardSyncronizedScreen;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVActor;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;

import java.util.ArrayList;
import java.util.List;

import math.latlng.LatLng;
import math.vector.Vector3;

/**
 * Created by nik on 29.04.17.
 */

public class MyGameScreen extends CRVHardSyncronizedScreen {

    private ARActivity context;
    private List<Attraction> attractions;
    Attraction base;

    public void setTestContext(ARActivity context) {
        this.context = context;
    }

    @Override
    public void resume() {
        super.resume();
        int i = 0;
        attractions = new ArrayList<>();
        ArrayList<Friend> friends = (ArrayList<Friend>)Shared.getBundle().put("selected_friends", new ArrayList<Friend>());
        for (Friend friend:
             friends) {
            Attraction attraction = base.clone();
            attraction.setFriend(friend);
            attractions.add(attraction);

        }
        int c = attractions.size();

        for(Attraction attraction: attractions) {
            attraction.prepare();
            final int progress = ((++i) * 100/c);
            CRVContext.executeInMainTread(new Runnable() {
                @Override
                public void run() {
                    context.onPrepareProgress(progress);
                }
            });
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private void translateToVector(Vector3 v, float[] data) {
        v.x = data[0];
        v.y = data[1];
        v.z = data[2];
    }

    @Override
    public void presentAfterSync(float deltaTime) {
        for(Attraction attraction: attractions) {
            attraction.present();
        }
    }

    @Override
    public void init(ResMap<String, Object> data) {
        base = ((List<Attraction>)data.get("attractions")).get(0);
        super.init(data);
    }
}