package com.ntunin.cybervision.android.graphics;

import android.graphics.Bitmap;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.Graphics;
import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.Pixmap;

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    Graphics.PixmapFormat format;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}