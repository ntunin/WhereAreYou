package com.ntunin.cybervision.virtualmanagement.crvlight;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by nick on 01.03.16.
 */
public class CRVAmbientLight extends CRVLight {
    float[] color = {1, 1, 1, 1};

    public CRVAmbientLight(int id) {
        super(id);
    }

    public CRVAmbientLight() {
        super();
    }

    public void setColor(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }
    public  void enable() {
        GL10 gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, color, 0);
    }
}
