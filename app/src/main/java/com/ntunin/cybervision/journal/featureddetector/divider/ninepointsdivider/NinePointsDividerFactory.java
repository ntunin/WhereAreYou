package com.ntunin.cybervision.journal.featureddetector.divider.ninepointsdivider;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.journal.featureddetector.divider.DividerFactory;

/**
 * Created by nikolay on 12.03.17.
 */

public class NinePointsDividerFactory extends DividerFactory implements Injectable {
    @Override
    protected Releasable create() {
        return new NinePointsDivider();
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
