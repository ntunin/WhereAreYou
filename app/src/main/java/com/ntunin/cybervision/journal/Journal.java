package com.ntunin.cybervision.journal;

import com.ntunin.cybervision.journal.breakingnews.BreakingNews;

/**
 * Created by nikolay on 26.01.17.
 */

public abstract class Journal {
    public abstract void release(String title, BreakingNews news);
    public abstract void subscribe(String title, JournalSubscriber subscriber);

    public abstract void release(int id, BreakingNews news);
    public abstract void subscribe(int id, JournalSubscriber subscriber);
}
