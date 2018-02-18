package com.ntunin.cybervision.io.xfile;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.io.xfile.xsreambuilder.XStreamBuilder;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nik on 20.06.16.
 */
public class XText implements XFactory {
    private static XText _factory = new XText();
    public static XText factory() {
        return _factory;
    }
    public CRVSkin loadFrame(XTextStreamReader reader) {
        CRVSkin document = XStreamBuilder.read(reader);
        return document;
    }

}
