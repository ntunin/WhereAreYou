package com.ntunin.cybervision.virtualmanagement.crvmotion;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

/**
 * Created by nikolay on 17.10.16.
 */

public class CRVRoll extends CRVTransition {
    @Override
    public void act(CRVBody b) {
        super.act(b);
        float roll = b.getRoll();
        gl.glRotatef(roll, 0, 0, 1);
    }
}
