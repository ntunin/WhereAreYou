package com.ntunin.cybervision.virtualmanagement.crvmotion;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

/**
 * Created by nikolay on 17.10.16.
 */

public class CRVYaw extends CRVTransition {
    @Override
    public void act(CRVBody b) {
        super.act(b);
        float yaw = b.getYaw();
        gl.glRotatef(yaw, 0, 1, 0);
    }
}
