package com.ntunin.cybervision.errno;

import com.ntunin.cybervision.crvcontext.CRVContext;
import com.ntunin.cybervision.res.Res;
import com.ntunin.cybervision.res.ResMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikolay on 05.10.16.
 */

public class ERRNO {
    private static ERRNO errno;
    private List<String> list;
    private ResMap<String, List<ErrorListener>> listeners = new ResMap<>();

    public static String last() {
        if(errno == null) errno = new ERRNO();
        return errno._last();
    }

    private ERRNO() {
        list = new LinkedList<>();
    }
    private String _last() {
        int index = list.size() - 1;
        if(index < 0) return null;
        return list.get(index);
    }
    public static void write(String description) {
        if(errno == null) errno = new ERRNO();
        errno._write(description);
    }

    public static void write(int id) {
        String description = Res.error(id);
        if(errno == null) errno = new ERRNO();
        errno._write(description);
    }

    private  void _write(final String description) {
        list.add(description);
        CRVContext.executeInMainTread(
                new Runnable() {
                    @Override
                    public void run() {
                        List<ErrorListener> listeners = (List<ErrorListener>) ERRNO.this.listeners.get(description);
                        if(listeners == null) {
                            return;
                        }
                        for(ErrorListener listener: listeners) {
                            listener.onError(description);
                        }
                    }
                }
        );
    }

    public static boolean isLast(String description) {
        if(errno == null) errno = new ERRNO();
        return errno._isLast(description);
    }

    public static boolean isLast(int id) {
        if(errno == null) errno = new ERRNO();
        return errno._isLast(Res.string(id));
    }

    private boolean _isLast(String description) {
        String last = _last();
        return last.equals(description);
    }

    public static void subscribe(int[] ids, ErrorListener listener) {
        for(int id: ids) {
            subscribe(id, listener);
        }
    }

    public static void subscribe(String[] errs, ErrorListener listener) {
        for(String err: errs) {
            subscribe(err, listener);
        }
    }

    public static void subscribe(int id, ErrorListener listener) {
        subscribe(Res.string(id), listener);
    }

    public static void subscribe(String err, ErrorListener listener) {
        if(errno == null) errno = new ERRNO();
        errno._subscribe(err, listener);
    }

    private void _subscribe(String err, ErrorListener listener) {
        List<ErrorListener> listeners = (List<ErrorListener>) this.listeners.get(err);
        if(listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(listener);
        this.listeners.put(err, listeners);
    }




}
