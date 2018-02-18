package com.ntunin.cybervision.android.io;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;

/**
 * Created by nik on 07.05.17.
 */

public class CRVCompassSensor extends CRVHardwareContextSensor implements Injectable {
    private float[] magnetData = new float[3];
    private int index;
    int smooth = 3;
    private float[][] buffer = new float[smooth][];
    private boolean modified = false;
    int readyIndex = 0;

    @Override
    public void init(ResMap<String, Object> data) {
        for(int i = 0; i < smooth; i++) {
            buffer[i] = new float[3];
        }
        setType(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        super.onSensorChanged(sensorEvent);
        float[] buffer = this.buffer[index];
        index = (index + 1) % smooth;
        float[] data = super.getData();
        buffer[0] = round(data[0], 10000);
        buffer[1] = round(data[1], 10000);
        buffer[2] = round(data[2], 10000);
        modified = true;
    }

    @Override
    public float[] getData() {
        if(smooth > readyIndex++) {
            return super.getData();
        }
        if(modified) {
            smooth();
        }
        return magnetData;
    }

    private void smooth() {
        set(magnetData, 0);
        for(int i = 0; i < smooth; i++) {
            float[] buffer = this.buffer[i];
            add(magnetData, buffer);
        }
        div(magnetData, smooth);
        modified = false;
    }

    private void set(float[] d, float v) {
        d[0] = v;
        d[1] = v;
        d[2] = v;
    }

    private void add(float[] d1, float[] d2) {
        d1[0] += d2[0];
        d1[1] += d2[1];
        d1[2] += d2[2];
    }

    private void div(float[] d, float v) {
        d[0] /= v;
        d[1] /= v;
        d[2] /= v;
    }

    private float round(float v, float r) {
        return (int)(v * r) / r;
    }
}
