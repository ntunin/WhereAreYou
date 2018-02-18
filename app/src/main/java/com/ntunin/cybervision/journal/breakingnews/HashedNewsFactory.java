package com.ntunin.cybervision.journal.breakingnews;

import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;

/**
 * Created by nikolay on 01.02.17.
 */

public class HashedNewsFactory extends NewsFactory implements Injectable{
    @Override
    protected String getTag() {
        return "news";
    }

    @Override
    public BreakingNews create() {
        return new HashedBreakingNews();
    }

    @Override
    public void init(ResMap<String, Object> data) {
        return;
    }
}
