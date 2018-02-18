package com.ntunin.cybervision.virtualmanagement.crvlight;
import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by nick on 01.03.16.
 */
public class CRVPointLight extends CRVLight {
    float[] position = { 0, 0, 0, 1 };
    public CRVPointLight() {
        super(0);
    }
    public CRVPointLight(int id) {
        super(id);
    }

    public void enable() {
        super.enable();
        GL10 gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
        gl.glLightfv(lightId, GL10.GL_POSITION, position, 0);
        lastLightId = lightId;
    }
    public void setPosition(float x, float y, float z) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

}
