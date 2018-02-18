package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.Map;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 08.10.16.
 */

public class XDynamicContent implements XVariable {
    String type;
    StringBuilder token;
    XDocumentContext context;
    Map<String, XVariable> factories;
    Map<String, Object> node;

    public XDynamicContent(String type) {
        this.type = type;
        this.token = new StringBuilder();
    }
    @Override
    public XNamedVariable read(XDocumentContext context) {
        this.context = context;
        factories = context.getFactories();
        node = context.getCurrentNode();
        XTextStreamReader stream = context.getStream();
        while(stream.hasNext()) {
            char c = stream.getChar();
            if(handle(c) > 0) break;
        }
        stream.skip(-1);
        return null;
    }


    private int handle(char c) {
        switch (c) {
            case '\n':
            case '\t':
            case '\r': {
                if(token.length() > 0) token = new StringBuilder();
                return 0;
            }
            case ' ': {
                if(token.length() == 0) break;
                String _token = token.toString();
                token = new StringBuilder();
                handleToken(_token);
                break;
            }
            case '}': {
                return 1;
            }
            default: token.append(c);

        }
        return 0;
    }

    private void handleToken(String token) {
        XVariable var = factories.get(token);
        if(var != null) {
            XNamedVariable variable = var.read(context);
            if(variable != null) {
                String name = variable.getName();
                Object value = variable.getValue();
                node.put(name, value);
            }
        }
    }
}
