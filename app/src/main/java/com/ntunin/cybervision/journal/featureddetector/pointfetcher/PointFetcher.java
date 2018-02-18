package com.ntunin.cybervision.journal.featureddetector.pointfetcher;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;
import com.ntunin.cybervision.journal.featureddetector.divider.Divider;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.EdgeRegister;

/**
 * Created by nikolay on 11.03.17.
 */

public class PointFetcher extends Releasable {

    private ImageFrame frame;
    protected Divider divider;

    public EdgeRegister start(ImageFrame frame) {
        this.frame = frame;
         return null;
    }

    @Override
    public PointFetcher init(Object... args) {
        return this;
    }

    @Override
    public void release() {
        super.release();
    }
}
