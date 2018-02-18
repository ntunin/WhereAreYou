package com.ntunin.cybervision.io.xfile.xsreambuilder;

/**
 * Created by nikolay on 06.10.16.
 */

interface XVariableFactory {
    XVariable instantiate(XDocumentContext context);
    XVariable instantiate();
}
