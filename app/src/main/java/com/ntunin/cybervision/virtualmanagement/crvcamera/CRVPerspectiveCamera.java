package com.ntunin.cybervision.virtualmanagement.crvcamera;


import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

import math.vector.Vector3;


import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.GLGraphics;

/**
 * Created by nikolay on 12.10.16.
 */

public class CRVPerspectiveCamera implements CRVCamera, Injectable {
    private Vector3 position;
    private Vector3 target;
    private Vector3 up;

    public CRVPerspectiveCamera() {
        this.position = new Vector3();
        this.target = new Vector3(0, 0, -1000);
        this.up = new Vector3(0, 1, 0);
    }
    public CRVPerspectiveCamera(Vector3 target) {
        this.position = new Vector3();
        this.target = target;
        this.up = new Vector3(0, 1, 0);
    }
    public CRVPerspectiveCamera(Vector3 position, Vector3 target) {
        this.position = position;
        this.target = target;
        this.up = new Vector3(0, 1, 0);
    }
    public CRVPerspectiveCamera(Vector3 position, Vector3 target, Vector3 up) {
        this.position = position;
        this.target = target;
        this.up = up;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
    }

    public void setUp(Vector3 up) {
        this.up = up;
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
        GLU.gluPerspective(gl, 63, 1f*width / height, 1, 1000);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        Vector3 p = position;
        Vector3 t = target;
        Vector3 u = up;
        GLU.gluLookAt(gl,
                p.x, p.y, p.z,
                t.x, t.y, t.z,
                u.x, u.y, u.z);
    }

    @Override
    public void stop() {
        GL10 gl = (GL10) CRVContext.get(R.string.gl);
        gl.glPopMatrix();
    }

    @Override
    public void init(ResMap<String, Object> data) {

    }
}
