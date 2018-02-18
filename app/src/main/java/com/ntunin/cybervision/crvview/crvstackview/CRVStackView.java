package com.ntunin.cybervision.crvview.crvstackview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.crvview.CRVView;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;

import java.util.Timer;
import java.util.TimerTask;


import math.intsize.Size;
import math.vector.Vector3;


/**
 * Created by mikhaildomrachev on 17.04.17.
 */

public class CRVStackView extends CRVView {

    private CRVImageFrameView frameView;
    private GLScreenView screenView;
    private CRVView view;
    private Vector3 acceleration;
    private Vector3 rotation;
    private ImageFrame frame;


    public CRVStackView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        if(this.isInEditMode()) {
            return;
        }
        this.view = this;
        frameView = new CRVHardwareCameraView(context);
        screenView = new GLScreenView(context);
        view.addView(frameView);
        view.addView(screenView);

    }

    @Override
    public void start() {
        if(this.isInEditMode()) {
            return;
        }
        startFrameView();
    }

    private void startFrameView() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CRVInjector injector = CRVInjector.main();
                Size size = (Size) CRVContext.create(R.string.int_size).init(view.getWidth(), view.getHeight());
                CRVContext.set(R.string.view_size, size);
                frameView.start();
            }
        }, 500);
    }




}
