package com.ntunin.cybervision.journal.cameracapturing;


import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.journal.breakingnews.BreakingNews;
import com.ntunin.cybervision.journal.Journal;

/**
 * Created by nikolay on 30.01.17.
 */

public abstract class JournalingCameraCapturing implements Injectable {

    private ImageFrame frame;
    private Journal journal;
    private CRVObjectFactory factory;
    private String tag;

    protected void handleFrame(ImageFrame frame) {
       // if(this.frame != null) return;
        this.frame = frame.clone();
        CRVObjectFactory factory = (CRVObjectFactory) CRVInjector.main().getInstance(R.string.object_factory);
        BreakingNews news = (BreakingNews) factory.get(R.string.news).init();
        news.write(R.string.image_frame, frame);
        journal.release(tag, news);
        news.release();
    }

    public abstract void start();

    @Override
    public void init(ResMap<String, Object> args) {
        tag = (String) args.get(R.string.camera_action);
        journal = (Journal) args.get(R.string.journal);
        factory = (CRVObjectFactory) args.get(R.string.object_factory);
    }
}
