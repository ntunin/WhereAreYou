package com.ntunin.cybervision.io.xfile.xsreambuilder;

/**
 * Created by nikolay on 06.10.16.
 */

class XTemplateFactory implements XVariable {


    @Override
    public XNamedVariable read(XDocumentContext context) {
        new XTemplate(context);
        return null;
    }

}
