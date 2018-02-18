package com.ntunin.cybervision.android.io;

import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.crvcontext.PermissionListener;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.crvinjector.Injectable;
import com.ntunin.cybervision.journal.cameracapturing.ImageFrame;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.res.Res;
import com.ntunin.cybervision.res.ResMap;
import com.ntunin.cybervision.crvinjector.CRVInjector;

import math.intsize.Size;

/**
 * This class is an implementation of the Bridge View between OpenCV and Java Camera.
 * This class relays on the functionality available in base class and only implements
 * required functions:
 * connectCamera - opens Java camera and sets the PreviewCallback to be delivered.
 * disconnectCamera - closes the camera and stops preview.
 * When frame is delivered via callback from Camera - it processed via OpenCV to be
 * converted to RGBA32 and then passed to the external callback for modifications if required.
 */
public class CRVHardwareCamera implements PreviewCallback, Injectable {

    private static final int MAGIC_TEXTURE_ID = 10;
    private byte mBuffer[];
    private int mChainIdx = 0;
    private Thread mThread;
    private boolean mStopThread;

    private Camera mCamera;
    private ImageFrame[] mImageFrameChain;
    private ImageFrame frame;
    private SurfaceTexture mSurfaceTexture;

    private static final String TAG = "CameraCapturing";
    private static final int MAX_UNSPECIFIED = -1;
    private static final int STOPPED = 0;

    protected int mFrameWidth;
    protected int mFrameHeight;
    protected int mMaxHeight;
    protected int mMaxWidth;
    protected int mCameraIndex = CAMERA_ID_ANY;

    public static final int CAMERA_ID_ANY   = -1;
    public static final int CAMERA_ID_BACK  = 99;
    public static final int CAMERA_ID_FRONT = 98;



    public interface ListItemAccessor {
        public int getWidth(Object obj);
        public int getHeight(Object obj);
    };

    public void start() {
        if(!hasSystemFeature()) {
            onError();
            return;
        }
        if(isPermissionGranted()) {
           onStartResolve();
        } else {
           requestCameraPermissions();
        };
    }

    private void onError() {
        ERRNO.write(R.string.camera_connection_error);
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(CRVContext.current(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasSystemFeature() {
        PackageManager pm = CRVContext.current().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void requestCameraPermissions() {
        CRVContext.sendGrantRequest(
                new String[]{Manifest.permission.CAMERA},
                R.string.camera_permission,
                new PermissionListener() {
                    @Override
                    public void onPermissionResult(int[] results) {
                        if(results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                            onStartResolve();
                        } else {
                            ERRNO.write(R.string.camera_not_granted);
                        }
                    }
                }
        );

    }

    private void onStartResolve() {
        Size size = (Size) CRVInjector.main().getInstance(R.string.view_size);
        connectCamera(size.width, size.height);
    }

    public void stop() {
        disconnectCamera();
    }

    public static class JavaCameraSizeAccessor implements ListItemAccessor {

        @Override
        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        @Override
        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }

    @Override
    public void init(ResMap<String, Object> args) {
        mCameraIndex = (int) args.get(R.string.camera_id);
        mMaxWidth = MAX_UNSPECIFIED;
        mMaxHeight = MAX_UNSPECIFIED;
    }

    protected boolean initializeCamera() {
        Log.d(TAG, "Initialize java camera");
        boolean result = true;
        synchronized (this) {
            mCamera = null;
            if (mCameraIndex == CAMERA_ID_ANY) {
                Log.d(TAG, "Trying to open camera with old open()");
                try {
                    mCamera = Camera.open();
                }
                catch (Exception e){
                    Log.e(TAG, "Camera is not available (in use or does not exist): " + e.getLocalizedMessage());
                }

                if(mCamera == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    boolean connected = false;
                    for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                        Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(camIdx) + ")");
                        try {
                            mCamera = Camera.open(camIdx);
                            connected = true;
                        } catch (RuntimeException e) {
                            Log.e(TAG, "Camera #" + camIdx + "failed to open: " + e.getLocalizedMessage());
                        }
                        if (connected) break;
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    int localCameraIndex = mCameraIndex;
                    if (mCameraIndex == CAMERA_ID_BACK) {
                        Log.i(TAG, "Trying to open back camera");
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo( camIdx, cameraInfo );
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                localCameraIndex = camIdx;
                                break;
                            }
                        }
                    } else if (mCameraIndex == CAMERA_ID_FRONT) {
                        Log.i(TAG, "Trying to open front camera");
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo( camIdx, cameraInfo );
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                localCameraIndex = camIdx;
                                break;
                            }
                        }
                    }
                    if (localCameraIndex == CAMERA_ID_BACK) {
                        Log.e(TAG, "Back camera not found!");
                    } else if (localCameraIndex == CAMERA_ID_FRONT) {
                        Log.e(TAG, "Front camera not found!");
                    } else {
                        Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(localCameraIndex) + ")");
                        try {
                            mCamera = Camera.open(localCameraIndex);
                        } catch (RuntimeException e) {
                            Log.e(TAG, "Camera #" + localCameraIndex + "failed to open: " + e.getLocalizedMessage());
                        }
                    }
                }
            }

            if (mCamera == null)
                return false;

            /* Now set camera parameters */
            try {
                Camera.Parameters params = mCamera.getParameters();
                Log.d(TAG, "getSupportedPreviewSizes()");
                List<android.hardware.Camera.Size> sizes = params.getSupportedPreviewSizes();

                if (sizes != null) {
                    /* Select the size that fits surface considering maximum size allowed */
                    Size viewSize = (Size) CRVContext.get(R.string.view_size);
                    Size frameSize = calculateCameraFrameSize(sizes, new JavaCameraSizeAccessor(), viewSize);

                    params.setPreviewFormat(ImageFormat.NV21);
                    Log.d(TAG, "Set preview size to " + Integer.valueOf((int)frameSize.width) + "x" + Integer.valueOf((int)frameSize.height));
                    params.setPreviewSize((int)frameSize.width, (int)frameSize.height);

                    frameSize.release();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !android.os.Build.MODEL.equals("GT-I9100"))
                        params.setRecordingHint(true);

                    List<String> FocusModes = params.getSupportedFocusModes();
                    if (FocusModes != null && FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                    {
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    }

                    mCamera.setParameters(params);
                    params = mCamera.getParameters();

                    mFrameWidth = params.getPreviewSize().width;
                    mFrameHeight = params.getPreviewSize().height;

                    int size = mFrameWidth * mFrameHeight;
                    size  = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
                    mBuffer = new byte[size];

                    mCamera.addCallbackBuffer(mBuffer);
                    mCamera.setPreviewCallbackWithBuffer(this);

                    mImageFrameChain = new ImageFrame[2];
                    mImageFrameChain[0] = createFrame();
                    mImageFrameChain[1] = createFrame();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mSurfaceTexture = new SurfaceTexture(MAGIC_TEXTURE_ID);
                        mCamera.setPreviewTexture(mSurfaceTexture);
                    } else
                        mCamera.setPreviewDisplay(null);

                    /* Finally we are ready to start the preview */
                    Log.d(TAG, "startPreview");
                    mCamera.startPreview();
                }
                else
                    result = false;
            } catch (Exception e) {
                result = false;
                ERRNO.write(R.string.camera_connection_error);
            }
        }

        return result;
    }

    private ImageFrame createFrame() {
        ImageFrame frame = (ImageFrame) CRVContext.create(Res.string(R.string.image_frame)).init(mFrameWidth, mFrameHeight);
        return frame;
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);

                mCamera.release();
            }
            mCamera = null;
        }
    }

    private boolean mCameraFrameReady = false;

    protected boolean connectCamera(int width, int height) {

        /* 1. We need to instantiate camera
         * 2. We need to start thread which will be getting frames
         */
        /* First step - initialize camera connection */
        Log.d(TAG, "Connecting to camera");
        if (!initializeCamera())
            return false;

        mCameraFrameReady = false;

        /* now we can start update thread */
        Log.d(TAG, "Starting processing thread");
        mStopThread = false;
        mThread = new Thread(new CameraWorker());
        mThread.start();

        return true;
    }

    protected void disconnectCamera() {
        /* 1. We need to stop thread which updating the frames
         * 2. Stop camera and release it
         */
        Log.d(TAG, "Disconnecting from camera");
        try {
            mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                this.notify();
            }
            Log.d(TAG, "Wating for thread");
            if (mThread != null)
                mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mThread =  null;
        }

        /* Now release camera */
        releaseCamera();

        mCameraFrameReady = false;
    }

    @Override
    public void onPreviewFrame(byte[] frame, Camera arg1) {
        synchronized (this) {
            mImageFrameChain[mChainIdx].put(frame);
            mCameraFrameReady = true;
            this.notify();
        }
        if (mCamera != null)
            mCamera.addCallbackBuffer(mBuffer);
    }

    private class CameraWorker implements Runnable {

        @Override
        public void run() {
            do {
                boolean hasFrame = false;
                synchronized (CRVHardwareCamera.this) {
                    try {
                        while (!mCameraFrameReady && !mStopThread) {
                            CRVHardwareCamera.this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mCameraFrameReady)
                    {
                        mChainIdx = 1 - mChainIdx;
                        mCameraFrameReady = false;
                        hasFrame = true;
                    }
                }

                if (!mStopThread && hasFrame) {
                    frame = mImageFrameChain[1 - mChainIdx];
                }
            } while (true);//(!mStopThread);
        }
    }

    public ImageFrame getFrame() {
        return frame;
    }

    protected Size calculateCameraFrameSize(List<?> supportedSizes, JavaCameraSizeAccessor accessor, Size surfaceSize) {
        int calcWidth = 0;
        int calcHeight = 0;

        int maxAllowedWidth = (mMaxWidth != MAX_UNSPECIFIED && mMaxWidth < surfaceSize.width)? mMaxWidth : surfaceSize.width;
        int maxAllowedHeight = (mMaxHeight != MAX_UNSPECIFIED && mMaxHeight < surfaceSize.height)? mMaxHeight : surfaceSize.height;

        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);

            if (width <= maxAllowedWidth && height <= maxAllowedHeight) {
                if (width >= calcWidth && height >= calcHeight) {
                    calcWidth = (int) width;
                    calcHeight = (int) height;
                }
            }
        }



        Size size = (Size) CRVContext.create(R.string.int_size).init(calcWidth, calcHeight);

        return size;
    }

}
