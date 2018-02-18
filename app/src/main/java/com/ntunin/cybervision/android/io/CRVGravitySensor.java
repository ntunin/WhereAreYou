package com.ntunin.cybervision.android.io;

import android.hardware.Sensor;

import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;

/**
 * Created by nik on 06.05.17.
 */

public class CRVGravitySensor extends CRVHardwareContextSensor implements Injectable {

    @Override
    public void init(ResMap<String, Object> data) {
        setType(Sensor.TYPE_GRAVITY);
    }
}
