package com.ntunin.cybervision.io.xfile.xsreambuilder;

/**
 * Created by nikolay on 07.10.16.
 */

class XNamedVariable {
    String name;
    Object value;

    public XNamedVariable(String name) {
        this.name = name;
    }
    public XNamedVariable() {

    }
    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
