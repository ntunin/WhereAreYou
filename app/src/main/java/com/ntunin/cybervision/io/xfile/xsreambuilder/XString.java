package com.ntunin.cybervision.io.xfile.xsreambuilder;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 07.10.16.
 */

class XString  extends XPrimitive {


    public XString(XDocumentContext context) {
        super(context);
    }
    public XString() {
        super();
    }

    @Override
    public XNamedVariable read(XDocumentContext context) {
        XTextStreamReader stream = context.getStream();
        StringBuilder builder = new StringBuilder();
        XStreamTokenizer.read(stream, '\"');
        String value = XStreamTokenizer.read(stream, '\"');
        XStreamTokenizer.read(stream, ';');
        stream.skip(-1);
        XNamedVariable result = new XNamedVariable(name);
        result.setValue(value);
        return result;
    }
}