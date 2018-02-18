package com.ntunin.cybervision.activity;

import android.location.Location;
import android.util.Log;

import com.ntunin.cybervision.android.io.CRVHardwareLocationListener;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvview.screen.CRVHardSyncronizedScreen;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVActor;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;

import java.util.List;

import math.latlng.LatLng;
import math.vector.Vector3;

/**
 * Created by nik on 29.04.17.
 */

public class MyGameScreen extends CRVHardSyncronizedScreen {

    private ERStackViewTestActivity context;
    private List<Attraction> attractions;

    public void setTestContext(ERStackViewTestActivity context) {
        this.context = context;
    }

    @Override
    public void resume() {
        super.resume();
        int i = 0;
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
        attractions = (List<Attraction>) data.get("attractions");
        super.init(data);
    }
}
