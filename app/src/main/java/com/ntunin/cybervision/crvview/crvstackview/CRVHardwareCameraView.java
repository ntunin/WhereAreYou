package com.ntunin.cybervision.crvview.crvstackview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.android.io.CRVHardwareCamera;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.journal.breakingnews.BreakingNews;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;
import com.ntunin.cybervision.journal.Journal;
import com.ntunin.cybervision.journal.JournalSubscriber;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nikolay on 05.02.17.
 */

public class CRVHardwareCameraView extends CRVImageFrameView implements JournalSubscriber{
    private CRVHardwareCamera camera;
    private Thread frameRenderWorker;
    private Timer renderTimer = new Timer();
    public CRVHardwareCameraView(Context context) {
        super(context);
        init(context);
    }

    public CRVHardwareCameraView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        CRVInjector injector = CRVInjector.main();
        Journal journal = (Journal) injector.getInstance(R.string.journal);
        if(journal == null) {
            ERRNO.write(R.string.no_journal);
            return;
        }
        journal.subscribe("Camera", this);
    }

    @Override
    public void start() {
        camera = (CRVHardwareCamera) CRVContext.get(R.string.camera);
        if(camera == null) {
            ERRNO.write(R.string.no_frame_service);
            return;
        }
        camera.start();
        drawCameraFrame();
        frameRenderWorker = new Thread(new FrameRenderWorker());
        frameRenderWorker.start();
    }

    private void drawCameraFrame() {
        renderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final ImageFrame frame = camera.getFrame();
                if(frame == null) {
                    drawCameraFrame();
                    return;
                }
                CRVContext.executeInMainTread(new Runnable() {
                    @Override
                    public void run() {
                        draw(frame);
                        drawCameraFrame();
                    }
                });
            }
        }, 20);
    }



    private class FrameRenderWorker implements Runnable {

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void breakingNews(BreakingNews news) {
        ImageFrame frame = (ImageFrame) news.read(R.string.image_frame);
        draw(frame);
    }


}
