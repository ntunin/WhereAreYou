package com.ntunin.cybervision.journal.featureddetector.divider;

/**
 * Created by nikolay on 18.03.17.
 */

public abstract class DividerDelegate {
    public abstract boolean addPoint(int x, int y);
    public abstract int getDirection();
}
