package com.ntunin.cybervision.journal.cameracapturing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;

/**
 * Created by nikolay on 11.02.17.
 */

public class YCbCrFrame extends ImageFrame {
    private int squire;
    private double Y, Cb, Cr;
    private int offset;

    private static final double a = 1.370705, b = 0.698001, c = 0.337633, d = 1.732446;
    static float div = 0.001f;

    public YCbCrFrame init(Object... args) {
        super.init(args);
        squire = size.width * size.height;
        int length = (int) (1.5 * squire + size.width);
        if(this.data == null || length != this.data.length) {
            byte data[] = new byte[length];
            put(data);
        }
        return this;
    }

    @Override
    public void put(int x, int y, int R, int G, int B) {
        if(data == null) return;
        if(x < 0) x = 0;
        if(x > size.width - 1) x = size.width - 1;
        if(y < 0) y = 0;
        if(y > size.height - 1) y = size.height - 1;

        int offset = squire + (y/2) * size.width + 2 * (x/2);
        synchronized (this) {
            data[y * size.width + x] = (byte) Math.max(0, Math.min(255, (int) (16 + 0.257 * R + 0.504 * G + 0.098 * B)));
            data[offset + 1] =  (byte) Math.max(0, Math.min(255, (int) (128 - 0.148 * R - 0.291 * G + 0.439 * B )));
            data[offset] = (byte)Math.max(0, Math.min(255, (int) (128 + 0.439 * R - 0.268 * G - 0.071 * B)));
        }

    }

    @Override
    public int[] get(int x, int y) {
        if(data == null) return null;
        if(x < 0) x = 0;
        if(x > size.width - 1) x = size.width - 1;
        if(y < 0) y = 0;
        if(y > size.height - 1) y = size.height - 1;
        offset = squire + (y/2) * size.width + 2 * (x/2);
        synchronized (this) {
            Y = (data[y * size.width + x] & 0xFF - 16) * 1.164;
            Cb = data[offset + 1] & 0xFF - 128;
            Cr = data[offset] - 128;
        }
         return new int[]{
                 Math.max(0, Math.min(255, (int) (Y + 1.596 * Cr) - 16)),
                 Math.max(0, Math.min(255, (int) (Y - 0.392 * Cb - 0.813 * Cr) - 16)),
                 Math.max(0, Math.min(255, (int) (Y + 2.017 * Cb) - 16))
        };
    }

    @Override
    public int getBrightness(int x, int y) {
        if(data == null) return 0;
        if(x < 0) x = 0;
        if(x > size.width - 1) x = size.width - 1;
        if(y < 0) y = 0;
        if(y > size.height - 1) y = size.height - 1;
        return (int)((data[y * size.width + x] & 0xFF - 16) * 1.164);
    }

    @Override
    public Bitmap getBitmap() {
        synchronized (this) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, out);
            byte[] imageBytes = out.toByteArray();
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Matrix rotate = new Matrix();
            return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), rotate, true);
        }
    }

    @Override
    public void release() {
        super.release();
    }
}
