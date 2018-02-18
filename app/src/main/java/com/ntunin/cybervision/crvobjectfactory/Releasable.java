package com.ntunin.cybervision.crvobjectfactory;

/**
 * Created by nikolay on 11.03.17.
 */

public abstract class Releasable {

    private ReleasableDelegate delegate;
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void release() {
        delegate.release(this);
    }

    public void setDelegate(ReleasableDelegate delegate) {
        this.delegate = delegate;
    }

    public abstract Releasable init(Object... args);
}
