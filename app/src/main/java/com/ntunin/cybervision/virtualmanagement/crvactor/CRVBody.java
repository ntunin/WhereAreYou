package com.ntunin.cybervision.virtualmanagement.crvactor;


import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;

import math.vector.Vector3;

/**
 * Created by nikolay on 12.10.16.
 */

public class CRVBody {
    private float m;
    private Vector3 r = new Vector3();
    private Vector3 v = new Vector3();
    private Vector3 a = new Vector3();
    private Vector3 s = new Vector3(1, 1, 1);
    private float yaw, pitch, roll;

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public Vector3 getR() {
        return r;
    }
    public Vector3 getV() {
        return v;
    }

    public Vector3 getS() {
        return s;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setR(Vector3 r) {
        this.r = r;
    }


    public void setV(Vector3 v) {
        this.v = v;
    }
    public void addPitch(float pitch) {
        this.pitch += pitch;
    }

    public void addRoll(float roll) {
        this.roll += roll;
    }

    public void addYaw(float yaw) {
        this.yaw += yaw;
    }

    public void addR(Vector3 r) {
        this.r = this.r.add(r);
    }


    public void addV(Vector3 v) {
        this.v = this.v.add(v);
    }

    public void setScale(Vector3 s) {
        this.s = s;
    }

    public  CRVBody clone() {
        CRVBody b = new CRVBody();
        b.setPitch(pitch);
        b.setRoll(roll);
        b.setScale(s);
        b.setR(r);
        b.setV(v);
        b.setYaw(yaw);
        return b;
    }
}
