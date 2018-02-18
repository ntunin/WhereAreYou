package com.ntunin.cybervision.journal.featureddetector;

import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.journal.Journal;
import com.ntunin.cybervision.journal.JournalSubscriber;
import com.ntunin.cybervision.journal.breakingnews.BreakingNews;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.PointFetcher;
import com.ntunin.cybervision.journal.featureddetector.pointfetcher.edge.EdgeRegister;

/**
 * Created by nikolay on 12.03.17.
 */

public class Detector implements JournalSubscriber, Injectable {
    private CRVObjectFactory factory;
    private Journal journal;
    private PointFetcher fetcher;



    @Override
    public void breakingNews(BreakingNews news) {
        ImageFrame frame = (ImageFrame) news.read(R.string.image_frame);
        EdgeRegister table = fetcher.start(frame);
        fetcher.release();
        news.write(R.string.edge_register, table);
        Journal journal = (Journal) CRVInjector.main().getInstance(R.string.journal);
        journal.release(R.string.markup, news);
    }

    @Override
    public void init(ResMap<String, Object> data) {
        factory = (CRVObjectFactory) data.get(R.string.object_factory);
        journal = (Journal) data.get(R.string.journal);
        fetcher = (PointFetcher) factory.get(R.string.point_fetcher).init();
        String action = (String) data.get(R.string.position_action);
        journal.subscribe(action, this);
    }
}
