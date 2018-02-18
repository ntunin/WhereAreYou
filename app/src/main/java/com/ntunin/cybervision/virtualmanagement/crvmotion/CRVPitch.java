package com.ntunin.cybervision.virtualmanagement.crvmotion;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

/**
 * Created by nikolay on 17.10.16.
 */

public class CRVPitch extends CRVTransition {
    @Override
    public void act(CRVBody b) {
        super.act(b);
        float pitch = b.getPitch();
        gl.glRotatef(pitch, 1, 0, 0);
    }
}
