package com.ntunin.cybervision.io.xfile.xsreambuilder;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 07.10.16.
 */

class XStreamTokenizer {
    public static String read(XTextStreamReader stream, char stop) {
        return new XStreamTokenizer()._read(stream, stop);
    }

    private XStreamTokenizer() {

    }

    private String _read(XTextStreamReader stream, char stop) {
        StringBuilder builder = new StringBuilder();
        while(true) {
            char c = stream.getChar();
            if (builder.length() == 0 && (c == ' ' || c == '\n' || c == '\r' || c == '\t')) continue;
            if(c == stop) break;
            builder.append(c);
        }
        return builder.toString().trim();
    }
}
