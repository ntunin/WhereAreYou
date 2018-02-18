package com.ntunin.cybervision.android.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ntunin.cybervision.crvcontext.CRVContext;


public class AndroidFastRenderView extends SurfaceView implements Runnable {
    CRVContext game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public AndroidFastRenderView(CRVContext game, Bitmap framebuffer) {
        super((Context)game); //it must be mistake
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running) {
            if(!holder.getSurface().isValid())
                continue;
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();
//            game.getCurrentScreen().update(deltaTime);
//            game.getCurrentScreen().present(deltaTime);
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }
    public void pause() {
        running = false;
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
// повтор
            }
        }
    }
}