package org.themarioga.cclh.enums;

public enum GameTypeEnum {

    DEMOCRACY(1), CLASSIC(2), DICTATORSHIP(3);

    private int type;

    GameTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
