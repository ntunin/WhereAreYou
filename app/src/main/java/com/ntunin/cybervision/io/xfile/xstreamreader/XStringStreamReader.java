package com.ntunin.cybervision.io.xfile.xstreamreader;


import com.ntunin.cybervision.errno.ERRNO;

/**
 * Created by Николай on 30.07.2016.
 */
public class XStringStreamReader implements XTextStreamReader {
    protected StringBuilder stream;
    int index = 0;
    public XStringStreamReader(String stream) {
        this.stream = new StringBuilder(stream);
    }

    @Override
    public String getString(int length) {
        int streamLength = stream.length();
        if(index > streamLength) {
            index += length;
            return "";
        }
        int last = index + length;
        if(last > streamLength) {
            last = streamLength;
        }
        String result = stream.subSequence(index, last).toString();
        index+=length;
        return result;
    }

    @Override
    public char getChar() {
        try {
            return stream.charAt(index++);
        } catch (StringIndexOutOfBoundsException e) {
            ERRNO.write("Read out end of stream");
        }
        return '\10';
    }

    @Override
    public boolean hasNext() {
        return index < stream.length();
    }

    @Override
    public int offset() {
        return index;
    }

    @Override
    public void skip(int relative) {
        index += relative;
    }
}
