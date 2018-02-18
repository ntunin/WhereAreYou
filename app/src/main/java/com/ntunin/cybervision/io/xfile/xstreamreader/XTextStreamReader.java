package com.ntunin.cybervision.io.xfile.xstreamreader;

/**
 * Created by Николай on 30.07.2016.
 */
public interface XTextStreamReader {
    public String getString(int length);
    public char getChar();
    public boolean hasNext();
    public int offset();
    public void skip(int relative);
}
