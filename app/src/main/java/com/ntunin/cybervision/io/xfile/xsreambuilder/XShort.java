package com.ntunin.cybervision.io.xfile.xsreambuilder;


import com.ntunin.cybervision.errno.ERRNO;

/**
 * Created by nikolay on 07.10.16.
 */

class XShort extends XPrimitive {


    public XShort(XDocumentContext context) {
        super(context);
    }
    public XShort() {
        super();
    }
    @Override
    protected Object parse(String string) {
        try {
            return Short.parseShort(string);
        } catch (Exception e) {
            ERRNO.write("Parse Float");
            return null;
        }
    }
}
