package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay on 07.10.16.
 */

class XArrayFactory implements XVariable, XVariableFactory {

    private Map<String, XVariable> arraysFactories;
    public XArrayFactory() {
        arraysFactories = new HashMap<>();

    }
    @Override
    public XNamedVariable read(XDocumentContext context) {
        return null;
    }

    @Override
    public XVariable instantiate(XDocumentContext context) {
        return new XArray(context);
    }

    @Override
    public XVariable instantiate() {
        return new XArray();
    }
}
