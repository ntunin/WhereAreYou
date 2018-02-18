package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.Map;

import com.ntunin.cybervision.errno.ERRNO;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 07.10.16.
 */

class XArray implements XVariable {
    XVariable itemReader;
    String name;
    int size;
    String sizeReference;

    public XArray(XDocumentContext context) {
        StringBuilder builder = new StringBuilder();
        XTextStreamReader stream = context.getStream();
        String type = XStreamTokenizer.read(stream, ' ');
        Map<String, XVariable> factories = context.getFactories();

        itemReader = ((XVariableFactory)factories.get(type)).instantiate();
        name = XStreamTokenizer.read(stream, '[');
        sizeReference = XStreamTokenizer.read(stream, ']');
        try {
            size = Integer.parseInt(sizeReference);
        } catch (Exception e) {
            size = -1;
        }
        XStreamTokenizer.read(stream, ';');
    }
    public XArray() {

    }
    @Override
    public XNamedVariable read(XDocumentContext context) {
        Map<String, Object> node = context.getCurrentNode();
        int size_0 = size;
        try {
            if(size < 0) size = (int) node.get(sizeReference);
        } catch (Exception e) {
            ERRNO.write("Parse Integer");
            return null;
        }
        Object[] array = new Object[size];
        for(int i = 0; i < size; i++) {
            XNamedVariable variable = itemReader.read(context);
            array[i] = variable.getValue();
            XTextStreamReader stream = context.getStream();
            char outsider = stream.getChar();
            if(outsider == ';') stream.skip(-1);
        }

        XTyped typedResult = new XTyped("array", name, array);
        XNamedVariable result = new XNamedVariable(name);
        result.setValue(typedResult);
        size = size_0;
        return result;
    }
}
