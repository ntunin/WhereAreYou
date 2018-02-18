package com.ntunin.cybervision.io.xfile;

import java.util.ArrayList;
import java.util.List;

import com.ntunin.cybervision.delegate.Delegate;
import com.ntunin.cybervision.delegate.DelegateException;
import com.ntunin.cybervision.delegate.DelegatingObject;
import com.ntunin.cybervision.io.GameIOException;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nik on 20.06.16.
 */
public abstract class XWatchBatcher extends DelegatingObject {
    List<XWatcher> watchers;
    protected XTextStreamReader reader;
    private StringBuilder contentBuilder = new StringBuilder();
    private boolean opened = true;
    private Delegate streamOut;
    private List<Delegate> modifiers = new ArrayList<>();

    public void setStreamOut(Delegate streamOut) {
        this.streamOut = streamOut;
    }

    public XWatchBatcher(XTextStreamReader reader) {
        init(reader);
    }

    private void init(XTextStreamReader reader) {
        this.reader = reader;
        try {
            this.watchers = getPrimitiveWatchers();
        } catch (DelegateException e) {
            e.printStackTrace();
        }
    }

    protected abstract List<XWatcher> getPrimitiveWatchers() throws DelegateException;

    public void read() throws GameIOException {
        opened = true;
        while(opened) {
            if(reader == null) throw new GameIOException("Nothing to read");
            if(!reader.hasNext() && streamOut != null) {
                try {
                    streamOut.invoke();
                    stop();
                    return;
                } catch (DelegateException e) {
                    throw new GameIOException("Exception with end of stream handle");
                }
            }
            char c = reader.getChar();
            try {
                modify();
            } catch (DelegateException e) {
                throw new GameIOException("Internal delegate exception");
            }
            if(!handleWatchers(this, c))
                contentBuilder.append(c);

        }
    }

    protected boolean handleWatchers(XWatchBatcher node, char in) throws GameIOException {
        if(node.watchers == null) return false;
        boolean handled = false;
        for(XWatcher watcher: node.watchers) {
            try {
                handled = handled || watcher.handle(in);
            } catch (DelegateException e) {
                throw new GameIOException(e.getMessage());
            }
        }
        return handled;
    }
    protected void unwatch() {
        watchers = new ArrayList<>();
    }

    public String getContent() {
        return contentBuilder.toString().trim();
    }


    public void clear() {
        contentBuilder = new StringBuilder();
    }
    public void stop() {
        opened = false;
    }
    public void subscribe(Delegate modifier) {
        modifiers.add(modifier);
    }
    private void modify() throws DelegateException {
        for(Delegate modifier: modifiers) {
            modifier.invoke();
        }
        modifiers = new ArrayList<>();
    }
    public void updateWatchers(List<XWatcher> watchers) {
        this.watchers = watchers;
    }
    public void addWatcher(XWatcher watcher) {
        watchers.add(watcher);
    }
    public void removeWatcher(XWatcher watcher) {
        watchers.remove(watcher);
    }
    public void setReader(XTextStreamReader reader) {
        this.reader = reader;
    }
    public void skip() {
        reader.skip(-1);
    }
}
