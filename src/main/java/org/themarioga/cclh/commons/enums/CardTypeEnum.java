package org.themarioga.cclh.commons.enums;

public enum CardTypeEnum {

    WHITE(1), BLACK(2);

    private int type;

    CardTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
