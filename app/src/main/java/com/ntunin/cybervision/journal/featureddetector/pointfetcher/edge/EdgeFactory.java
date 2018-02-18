package com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.crvobjectfactory.ReleasableFactory;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;

/**
 * Created by nikolay on 12.03.17.
 */

public class EdgeFactory extends ReleasableFactory implements Injectable {

    @Override
    protected String getTag() {
        return "Edge";
    }

    @Override
    protected Releasable create() {
        return new Edge();
    }

    @Override
    public void init(ResMap<String, Object> data) {

    }
}
