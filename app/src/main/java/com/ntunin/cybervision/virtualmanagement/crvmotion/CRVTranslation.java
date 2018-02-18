package com.ntunin.cybervision.virtualmanagement.crvmotion;

import math.vector.Vector3;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;

/**
 * Created by nikolay on 17.10.16.
 */

public class CRVTranslation extends CRVTransition {

    @Override
    public void act(CRVBody b) {
        super.act(b);
        Vector3 r = b.getR();
        gl.glTranslatef(r.x, r.y, r.z);
    }
}
