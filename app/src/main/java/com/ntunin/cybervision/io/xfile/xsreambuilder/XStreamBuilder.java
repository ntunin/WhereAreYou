package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.HashMap;
import java.util.Map;

import com.ntunin.cybervision.virtualmanagement.crvactor.CRVSkin.CRVSkin;
import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 05.10.16.
 */

public class XStreamBuilder {
    private StringBuilder token = new StringBuilder();
    private Map<String, XVariable> factories;
    private XTextStreamReader stream;
    private Map<String, Object> node = new HashMap<>();
    private XDocumentContext context;

    public static CRVSkin read(XTextStreamReader stream) {
        XStreamBuilder builder = new XStreamBuilder(stream);
        return builder.read();
    }

    private XStreamBuilder(XTextStreamReader stream) {
        this.context = new XDocumentContext();
        this.context.setStream(stream);
        this.stream = stream;
        this.factories = context.getFactories();
    }

    private CRVSkin read() {
        XTextStreamReader stream = context.getStream();
        node = readHierarchy(stream);
        XTyped document = wrapNode(node);
        CRVSkin frame= XFrameBuilder.read(document);
        return frame;
    }

    private Map<String, Object> readHierarchy(XTextStreamReader stream) {
        while(stream.hasNext()) {
            char c = stream.getChar();
            handle(c);
        }
        return node;
    }
    private void handle(char c) {
        switch (c) {
            case '\n':
            case '\t':
            case '\r': {
                if(token.length() > 0) token = new StringBuilder();
                return;
            }
            case ' ': {
                if(token.length() == 0) break;
                String _token = token.toString();
                token = new StringBuilder();
                handleToken(_token);
                break;
            }
            default: token.append(c);

        }
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

    private XTyped wrapNode(Map<String, Object> node) {
        return new XTyped("Frame", "document", node);
    }
}
