package com.ntunin.cybervision.io.xfile.xsreambuilder;

/**
 * Created by nikolay on 07.10.16.
 */

class XStringFactory  implements XVariable, XVariableFactory {

    @Override
    public XNamedVariable read(XDocumentContext context) {
        return null;
    }

    @Override
    public XVariable instantiate(XDocumentContext context) {
        return new XString(context);
    }

    @Override
    public XVariable instantiate() {
        return new XString();
    }
}