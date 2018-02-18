package com.ntunin.cybervision.io.xfile.xsreambuilder;

/**
 * Created by nikolay on 05.10.16.
 */

class XFloatFactory implements  XVariable, XVariableFactory {

    @Override
    public XNamedVariable read(XDocumentContext context) {
        return null;
    }

    @Override
    public XVariable instantiate(XDocumentContext context) {
        return new XFloat(context);
    }

    @Override
    public XVariable instantiate() {
        return new XFloat();
    }
}
