package com.ntunin.cybervision.journal.featureddetector.divider;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;

/**
 * Created by nikolay on 12.03.17.
 */

public abstract class Divider extends Releasable {
    protected ImageFrame frame;

    public void set(ImageFrame frame) {
        this.frame = frame;
    }
    public abstract void handle(int x, int y);
}
