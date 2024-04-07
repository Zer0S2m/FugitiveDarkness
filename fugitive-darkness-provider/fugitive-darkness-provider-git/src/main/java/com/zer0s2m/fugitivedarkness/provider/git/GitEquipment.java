package com.zer0s2m.fugitivedarkness.provider.git;

public enum GitEquipment {

    HEAD_FILE("HEAD"),

    REMOTE_TREE("tree"),

    FOLDER(".git");

    private final String value;

    GitEquipment(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
