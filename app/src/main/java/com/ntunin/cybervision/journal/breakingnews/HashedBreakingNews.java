package com.ntunin.cybervision.journal.breakingnews;

import com.ntunin.cybervision.crvobjectfactory.Releasable;
import com.ntunin.cybervision.res.Res;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay on 01.02.17.
 */

public class HashedBreakingNews extends BreakingNews {
    private Map<String, Releasable> data;

    public HashedBreakingNews() {
        data = new HashMap<>();
    }

    public void write(String title, Releasable news) {
        synchronized (this) {
            data.put(title, news);
        }
    }

    public Releasable read(String title) {
        return data.get(title);
    }

    @Override
    public void write(int id, Releasable news) {
        write(Res.string(id), news);
    }

    @Override
    public Releasable read(int id) {
        return read(Res.string(id));
    }

    @Override
    public Releasable init(Object... args) {
        synchronized (this) {
            return this;
        }
    }

    @Override
    public void release() {
        synchronized (this) {
            super.release();
            for(String tag: data.keySet()) {
                Releasable r = data.get(tag);
                r.release();
            }
        }
    }
}
