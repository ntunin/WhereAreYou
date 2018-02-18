package com.ntunin.cybervision.io.xfile;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.io.GameIOException;
import com.ntunin.cybervision.io.xfile.xsreambuilder.XStreamBuilder;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 19.08.16.
 */
public class XDocument {
    private static XDocument document;

    public static CRVSkin getFrame(XTextStreamReader reader) throws GameIOException {
        if(document == null) document = new XDocument();
        return document._getFrame(reader);
    }

    private CRVSkin _getFrame(XTextStreamReader stream) throws GameIOException {
        CRVSkin frame = XStreamBuilder.read(stream);
        return frame;
    }
    private XDocument() throws GameIOException {

    }

}
