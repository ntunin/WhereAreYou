package com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by nick on 01.03.16.
 */
public class Material {
    float[] ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
    float[] diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
    float[] specular = { 0.0f, 0.0f, 0.0f, 1.0f };
    private float power = 1;

    public void setAmbient(float r, float g, float b, float a) {
        setAmbient(new float[]{r, g, b, a});
    }
    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }
    public void setDiffuse(float r, float g, float b, float a) {
        setDiffuse(new float[]{r, g, b, a});
    }
    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }
    public void setSpecular(float r, float g, float b, float a) {
        setSpecular(new float[]{r, g, b, a});
    }
    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void enable(GL10 gl) {
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular, 0);
    }
}
