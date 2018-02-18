package com.ntunin.cybervision.virtualmanagement.crvmotion;

import javax.microedition.khronos.opengles.GL10;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVBody;
import com.ntunin.cybervision.crvinjector.CRVInjector;

/**
 * Created by nikolay on 17.10.16.
 */

public abstract class CRVTransition implements Injectable{
    protected GL10 gl;
    protected CRVTransition() {
    }
    public void act(CRVBody b) {
        if(gl == null) {
            gl = (GL10) CRVInjector.main().getInstance(R.string.gl);
        }
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
