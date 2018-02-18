package com.ntunin.cybervision.crvview.crvstackview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.crvcontext.CRVScreen;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.GLGraphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mikhaildomrachev on 18.04.17.
 */

public class GLScreenView extends FrameLayout implements GLSurfaceView.Renderer{

    private GLSurfaceView glView;
    private GLGraphics glGraphics;
    private int state = R.string.context_state_initialized;
    private Object stateChanged = new Object();
    private CRVScreen screen;
    private long startTime = System.nanoTime();

    public GLScreenView(Context context) {
        super(context);
        init(context);
    }

    public GLScreenView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        glView = new GLSurfaceView(context);
        //glView.setEGLContextClientVersion(2);
        glView.setEGLConfigChooser(8,8,8,8,16,0);
        glView.setRenderer(this);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glView.setZOrderOnTop(true);
        glGraphics = GLGraphics.create(glView);
        this.addView(glView);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        CRVInjector injector = CRVInjector.main();
        glGraphics.setGL(gl);
        injector.setInstance(R.string.graphics, glGraphics);
        injector.setInstance(R.string.gl, gl);
        synchronized (stateChanged) {
            if (state == R.string.context_state_initialized) {
                screen = (CRVScreen) CRVInjector.main().getInstance(R.string.screen);
                if(screen == null) {
                    ERRNO.write(R.string.no_screen);
                    return;
                }
            }
            state = R.string.context_state_running;
            screen.resume();
            startTime = System.nanoTime();
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        int state = -1;
        synchronized (stateChanged) {
            state = this.state;
        }
        switch (state) {
            case R.string.context_state_running: {
                onRun();
                break;
            }
            case R.string.context_state_paused: {
                onPause();
                break;
            }
            case R.string.context_state_finished: {
                onFinish();
                break;
            }
        }
    }

    private void onRun() {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - startTime) / 1000000000.0f;
        startTime = currentTime;
        screen.update(deltaTime);
        screen.present(deltaTime);
    }

    private void onPause() {
        screen.pause();
        synchronized (stateChanged) {
            this.state = R.string.context_state_idle;
            stateChanged.notifyAll();
        }
    }

    private void onFinish() {
        onPause();
        screen.dispose();
    }

}
