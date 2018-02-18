package com.ntunin.cybervision.virtualmanagement.crvlight;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by nick on 01.03.16.
 */
public class CRVDirectionalLight extends CRVLight {
    float[] direction = { 0, 0, -1, 0 };

    public CRVDirectionalLight() {
        super(0);
    }
    public CRVDirectionalLight(int id) {
        super(id);
    }

    public void enable(int lightId) {
        GL10 gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
        gl.glLightfv(lightId, GL10.GL_POSITION, direction, 0);
        lastLightId = lightId;
    }
    public void setDirection(float x, float y, float z) {
        direction[0] = -x;
        direction[1] = -y;
        direction[2] = -z;
    }
}
