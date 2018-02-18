package com.ntunin.cybervision.io.xfile.xsreambuilder;

import java.util.HashMap;
import java.util.Map;

import com.ntunin.cybervision.io.xfile.xstreamreader.XTextStreamReader;

/**
 * Created by nikolay on 06.10.16.
 */

class XDocumentContext {
    Map<String, XVariable> factories;
    XTextStreamReader stream;
    String lastToken;
    Map<String, Object> currentNode;

    public XDocumentContext() {
        factories = new HashMap<>();
        factories.put("FLOAT", new XFloatFactory());
        factories.put("DWORD", new XIntFactory());
        factories.put("WORD", new XShortFactory());
        factories.put("STRING", new XStringFactory());
        factories.put("template", new XTemplateFactory());
        factories.put("array", new XArrayFactory());
    }
    public Map<String, XVariable> getFactories() {
        return factories;
    }

    public void setStream(XTextStreamReader stream) {
        this.stream = stream;
    }
    public XTextStreamReader getStream() {
        return stream;
    }
    public String getLastToken() {
        return lastToken;
    }
    public void setLastToken(String token) {
        this.lastToken = token;
    }
    public void setCurrentNode(Map<String, Object> node) {
        this.currentNode = node;
    }

    public Map<String, Object> getCurrentNode() {
        return currentNode;
    }
}
