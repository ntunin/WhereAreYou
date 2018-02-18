package com.ntunin.cybervision.journal.cameracapturing;

import com.ntunin.cybervision.crvobjectfactory.ReleasableFactory;

/**
 * Created by nikolay on 11.02.17.
 */

public abstract class ImageFrameFactory extends ReleasableFactory {

    @Override
    protected String getTag() {
        return "Image Frame";
    }
}
