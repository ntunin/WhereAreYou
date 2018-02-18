package com.ntunin.cybervision.crvobjectfactory;

/**
 * Created by nikolay on 11.03.17.
 */

public abstract class ReleasableFactory {

    public Releasable get() {
        Releasable object = create();
        String tag = this.getTag();
        object.setTag(tag);
        return  object;
    }

    protected abstract String getTag();

    protected abstract Releasable create();
}
