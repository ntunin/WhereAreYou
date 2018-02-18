package com.ntunin.cybervision.virtualmanagement.crvmotion;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

import math.vector.Vector3;

/**
 * Created by nik on 21.06.17.
 */

public class CRVScale extends CRVTransition {
    @Override
    public void act(CRVBody b) {
        super.act(b);
        Vector3 scale = b.getS();
        gl.glScalef(scale.x, scale.y, scale.z);
    }
}
