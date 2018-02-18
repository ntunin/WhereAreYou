package com.ntunin.cybervision.io.xfile.xsreambuilder;


import com.ntunin.cybervision.errno.ERRNO;

/**
 * Created by nikolay on 06.10.16.
 */

class XFloat extends XPrimitive {

    public XFloat(XDocumentContext context) {
        super(context);
    }
    public XFloat() {
        super();
    }
    @Override
    protected Object parse(String string) {
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            ERRNO.write("Parse Float");
            return null;
        }
    }
}
