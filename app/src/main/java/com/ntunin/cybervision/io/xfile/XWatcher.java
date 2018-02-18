package com.ntunin.cybervision.io.xfile;

import com.ntunin.cybervision.delegate.Delegate;
import com.ntunin.cybervision.delegate.DelegateException;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nik on 20.06.16.
 */
public class XWatcher {
    private String lexema;
    private Delegate delegate;
    private StringBuilder chain;
    int offset;
    public XWatcher( String lexema, Delegate delegate ) {
        this.lexema = lexema;
        this.delegate = delegate;
        chain = new StringBuilder();
    }
    public String name() {
        return lexema;
    }
    public boolean handle(char in) throws DelegateException {
        chain.append(in);
        return trigger(check());
    }
    private boolean check() {
        return chain.charAt(offset) == lexema.charAt(offset);
    }
    private boolean trigger(boolean flag) throws DelegateException {
        if (flag) return handle();
        reset();
        return  false;
    }
    private boolean handle() throws DelegateException {
        if(!lexema.equals(chain.toString())) {
            offset++;
            return false;
        }
        reset();

        delegate.invoke(lexema);
        return true;
    }
    private void reset() {
        offset = 0;
        chain = new StringBuilder();
    }

    public XWatcher read(XTextStreamReader reader) {
        while(true) {

        }
    }

    public String getLexema() {
        return lexema;
    }
}
