package com.ntunin.cybervision.io.xfile.xsreambuilder;


import com.ntunin.cybervision.errno.ERRNO;

/**
 * Created by nikolay on 07.10.16.
 */

class XInt extends XPrimitive {


    public XInt(XDocumentContext context) {
        super(context);
    }
    public XInt() {
        super();
    }
    @Override
    protected Object parse(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            ERRNO.write("Parse Int");
            return null;
        }
    }
}
