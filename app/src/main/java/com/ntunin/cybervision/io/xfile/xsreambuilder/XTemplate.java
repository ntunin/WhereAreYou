package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 06.10.16.
 */

class XTemplate implements XVariable, XVariableFactory {

    private String type;
    private String id;
    private List<XVariable> definitions;
    private XDocumentContext context;

    public XTemplate(XDocumentContext context) {
        this.context = context;
        type = readType();
        id = readId();
        definitions = readContent();
        context.getFactories().put(type, this);
    }
    private String readType() {
        return XStreamTokenizer.read(context.getStream(), ' ');
    }
    private String readId() {
        XTextStreamReader stream = context.getStream();
        XStreamTokenizer.read(stream, '{');
        XStreamTokenizer.read(stream, '<');
        return XStreamTokenizer.read(stream, '>');
    }
    private List<XVariable> readContent() {
        XTextStreamReader stream = context.getStream();
        StringBuilder definition = new StringBuilder();
        definitions = new ArrayList<>();
        while(true) {
            char c = stream.getChar();
            switch (c) {
                case '}': {
                    return definitions;
                }
                case ' ': {
                    if(definition.length() == 0) break;
                    String type = definition.toString().trim();
                    definition = new StringBuilder();
                    XVariable var = parseDefinition(type, context);
                    if(var != null)
                        definitions.add(var);
                    break;
                }
                case '[': {
                    String type = XStreamTokenizer.read(stream, ']');
                    XVariable var = new XDynamicContent(type);
                    definitions.add(var);
                }
                case '\n':
                case '\t':
                case '\r':
                    break;

                default: {
                    definition.append(c);

                }
            }
        }
    }

    private XVariable parseDefinition(String type, XDocumentContext context) {
        XVariableFactory factory = (XVariableFactory) context.getFactories().get(type);
        if(factory == null) return null;
        XVariable var = factory.instantiate(context);
        return var;
    }

    @Override
    public XNamedVariable read(XDocumentContext context) {
        Map<String, Object> child = new HashMap<>();
        XTextStreamReader stream = context.getStream();

        String name = XStreamTokenizer.read(stream, '{');

        for(XVariable var: definitions) {
            context.setCurrentNode(child);
            XNamedVariable variable = var.read(context);
            if(variable == null) continue;
            String _name = variable.getName();
            Object value = variable.getValue();
            XStreamTokenizer.read(context.getStream(), ';');
            child.put(_name, value);
        }
        XStreamTokenizer.read(stream, '}');
        XNamedVariable result = new XNamedVariable(name);
        result.setValue(new XTyped(type, name, child));
        return result;
    }


    @Override
    public XVariable instantiate(XDocumentContext context) {
        XTextStreamReader stream = context.getStream();
        String name = XStreamTokenizer.read(stream, ';');
        stream.skip(-1);
        XNode node = new XNode(name, type);
        node.setDefinitions(definitions);
        return node;
    }

    @Override
    public XVariable instantiate() {
        XNode node = new XNode(null, type);
        node.setDefinitions(definitions);
        return node;
    }
}
