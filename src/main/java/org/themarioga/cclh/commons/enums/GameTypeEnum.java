package org.themarioga.cclh.commons.enums;

public enum GameTypeEnum {

    DEMOCRACY,
    CLASSIC,
    DICTATORSHIP;

    public static GameTypeEnum getEnum(int ordinal) {
        for (GameTypeEnum gameTypeEnum : values()) {
            if (gameTypeEnum.ordinal() == ordinal) {
                return gameTypeEnum;
            }
        }

        return null;
    }

}
