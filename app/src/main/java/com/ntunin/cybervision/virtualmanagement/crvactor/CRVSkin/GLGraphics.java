package com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;

/**
 * Created by nick on 01.03.16.
 */
public class GLGraphics {
    private static GLGraphics graphics;
    GLSurfaceView glView;
    GL10 gl;

    public static GLGraphics create(GLSurfaceView view) {
        graphics = new GLGraphics(view);
        CRVContext.set(R.string.graphics, graphics);
        return graphics;
    }

    private GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public static GL10 getGL() {
        return graphics.gl;
    }

    public static void setGL(GL10 gl) {
        if(graphics == null) return;
        graphics.gl = gl;
        CRVContext.set(R.string.gl, graphics.gl);
    }
    public static int getWidth() {
        if(graphics == null) return 0;
        int w = graphics.glView.getWidth();
        return w;
    }
    public static int getHeight() {
        if(graphics == null) return 0;
        return graphics.glView.getHeight();
    }
}
