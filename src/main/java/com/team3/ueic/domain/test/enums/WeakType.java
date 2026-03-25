package com.team3.ueic.domain.test.enums;

public enum WeakType {

    SYNONYM("동의어 찾기"),
    GRAMMAR("문법"),
    VOCABULARY("어휘"),
    CONTENT_MATCH("내용 일치"),
    SENTENCE_INSERT("문장 삽입");

    private final String label;

    WeakType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}