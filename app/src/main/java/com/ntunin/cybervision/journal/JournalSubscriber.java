package com.ntunin.cybervision.journal;

import com.ntunin.cybervision.journal.breakingnews.BreakingNews;

/**
 * Created by nikolay on 26.01.17.
 */

public interface JournalSubscriber {
    public void breakingNews(BreakingNews news);
}
