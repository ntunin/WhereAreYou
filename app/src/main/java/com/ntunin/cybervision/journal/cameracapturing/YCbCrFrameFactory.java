package com.ntunin.cybervision.journal.cameracapturing;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;

/**
 * Created by nikolay on 11.02.17.
 */

public class YCbCrFrameFactory extends ImageFrameFactory implements Injectable {

    @Override
    protected Releasable create() {
        return new YCbCrFrame();
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
