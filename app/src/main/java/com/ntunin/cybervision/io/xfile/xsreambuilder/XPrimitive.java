package com.ntunin.cybervision.io.xfile.xsreambuilder;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 07.10.16.
 */

abstract class XPrimitive implements XVariable

{
    protected String name;

    public XPrimitive(XDocumentContext context) {
        name = XStreamTokenizer.read(context.getStream(), ';');
    }
    public XPrimitive() {

    }
    @Override
    public XNamedVariable read(XDocumentContext context) {
        XTextStreamReader stream = context.getStream();
        StringBuilder builder = new StringBuilder();
        while (true) {
            char c = stream.getChar();
            if (c == ';' || c == ',' || c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                if(builder.length() == 0) {
                    continue;
                } else {
                    break;
                }
            }
            builder.append(c);
        }
        Object value = parse(builder.toString());
        stream.skip(-1);
        XNamedVariable result = new XNamedVariable(name);
        result.setValue(value);
        return result;
    }

    protected Object parse(String string) {
        return string;
    }
}
