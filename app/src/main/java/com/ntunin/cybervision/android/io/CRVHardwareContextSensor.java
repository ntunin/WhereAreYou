package com.ntunin.cybervision.android.io;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by nik on 06.05.17.
 */

public class CRVHardwareContextSensor extends CRVContextSensor {
    private int type;
    private float[] data;

    protected void setType(int type) {
        this.type = type;
    }

    public Sensor getHardwareSensor() {
        return getHardwareSensor(type);
    }

    public void startSensorTracking() {
        startSensorTracking(this, type);
    }


    public float[] getData() {
        return data;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        data = sensorEvent.values;
    }
}
