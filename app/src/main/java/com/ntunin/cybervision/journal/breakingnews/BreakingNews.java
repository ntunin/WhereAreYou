package com.ntunin.cybervision.journal.breakingnews;

import com.ntunin.cybervision.crvobjectfactory.Releasable;

/**
 * Created by nikolay on 01.02.17.
 */

public abstract class BreakingNews extends Releasable {
    public abstract void write(String title, Releasable news);
    public abstract Releasable read(String title);

    public abstract void write(int id, Releasable news);
    public abstract Releasable read(int id);
}
