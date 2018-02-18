package com.ntunin.cybervision.journal.featureddetector.pointfetcher.nautilus;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.PontFetcherFactory;

/**
 * Created by nikolay on 11.03.17.
 */

public class NautilusFactory extends PontFetcherFactory implements Injectable {
    @Override
    protected Releasable create() {
        return new Nautilus();
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
