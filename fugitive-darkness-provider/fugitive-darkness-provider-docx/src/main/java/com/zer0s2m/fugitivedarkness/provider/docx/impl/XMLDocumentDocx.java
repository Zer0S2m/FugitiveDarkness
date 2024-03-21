package com.zer0s2m.fugitivedarkness.provider.docx.impl;

enum XMLDocumentDocx {

    FOLDER("word"),

    FILE_NAME("document.xml"),

    EXPRESSION_XPATH("/*[name()='w:document']/*[name()='w:body']/*[name()='w:p']/*[name()='w:r']/*[name()='w:t']");

    private final String value;

    XMLDocumentDocx(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
