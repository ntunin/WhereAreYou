package com.ntunin.cybervision.virtualmanagement.crvcamera;


import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by nikolay on 12.10.16.
 */

public class CRVOrthographicCamera implements CRVCamera, Injectable {
    private float width;
    private float height;
    private float depth;
    public void OrthographicCamera() {
        width = 100;
        height = 100;
        depth = 100;
    }
    @Override
    public void motor() {

        GLGraphics glGraphics = (GLGraphics) CRVContext.get(R.string.graphics);
        int height = GLGraphics.getHeight();
        int width = GLGraphics.getWidth();
        GL10 gl = glGraphics.getGL();


        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(
                -this.width/2, this.width/2,
                -this.height/2, this.height/2,
                -this.depth/2, this.depth/2
        );
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void stop() {

    }

    @Override
    public void init(ResMap<String, Object> data) {
        ResMap<String, Object> argument = (ResMap<String, Object>) data.get("argument");
        depth = (float)(double)argument.get("depth");
        width = (float)(double)argument.get("width");
        height = (float)(double)argument.get("height");
    }
}
