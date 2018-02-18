package com.ntunin.cybervision.io.xfile.xstreamreader;

/**
 * Created by Николай on 30.07.2016.
 */
public interface XBinStreamReader {
    public String getString(int length);

    public int getInt();
    public short getShort();
    public char getChar();
    public Byte getByte();

    public double getDouble();
    public float getFloat();
    public boolean hasNext();
}
