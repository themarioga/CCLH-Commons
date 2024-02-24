package org.themarioga.cclh.commons.enums;

public enum GamePunctuationTypeEnum {

    POINTS,
    ROUNDS;

    public static GamePunctuationTypeEnum getEnum(int ordinal) {
        for (GamePunctuationTypeEnum gameTypeEnum : values()) {
            if (gameTypeEnum.ordinal() == ordinal) {
                return gameTypeEnum;
            }
        }

        return null;
    }

}
