package com.ntunin.cybervision.crvcontext;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import whereareyou.ntunin.com.whereareyou.R;
import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.crvinjector.CRVInjector;
import com.ntunin.cybervision.crvobjectfactory.CRVObjectFactory;
import com.ntunin.cybervision.crvobjectfactory.Releasable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class CRVContext extends AppCompatActivity {
    protected static CRVContext current;
    private PowerManager.WakeLock wakeLock;
    private CRVInjector injector;
    private CRVObjectFactory factory;
    private Map<Integer, List<PermissionListener>> permissionListeners;

    public static CRVContext current() {
        if(current == null) {
            ERRNO.write(R.string.no_context);
        }
        return current;
    }
    public static void setCurrent(CRVContext g) {
        current = g;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        current = this;
        permissionListeners = new HashMap<>();
        this.injector = CRVInjector.main();
        this.factory = (CRVObjectFactory) injector.getInstance(R.string.object_factory);
        CRVScreen screen =  getScreen();
        CRVInjector.main().setInstance(R.string.screen, screen);
    }


    protected void wakeLock() {
        PowerManager powerManager = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
                "GLGame");
    }

    public void catchFatal(String description) {
        ERRNO.write(description);
    }

    protected abstract CRVScreen getScreen();

    public static void executeInMainTread(Runnable code) {
        new Handler(current.getMainLooper()).post(code);
    }

    public static Releasable create(int id) {
        return current.factory.get(id);
    }

    public static Releasable create(String tag) {
        return current.factory.get(tag);
    }

    public static Object get(int id) {
        return current.injector.getInstance(id);
    }

    public static Object get(String tag) {
        return current.injector.getInstance(tag);
    }

    public static void set(int id, Object value) {
        current.injector.setInstance(id, value);
    }

    public static void set(String tag, Object value) {
        current.injector.setInstance(tag, value);
    }

    public static void sendGrantRequest(String[] permissions, int tag, PermissionListener listener) {
        List<PermissionListener> listeners = current.permissionListeners.get(tag);
        if(listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(listener);
        current.permissionListeners.put(tag, listeners);
        ActivityCompat.requestPermissions(current,
                permissions,
                tag);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<PermissionListener> listeners = this.permissionListeners.get(requestCode);
        for(PermissionListener listener: listeners) {
            listener.onPermissionResult(grantResults);
        }
        listeners.clear();
    }


}
