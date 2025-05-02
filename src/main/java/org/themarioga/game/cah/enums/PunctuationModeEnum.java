package org.themarioga.game.cah.enums;

public enum PunctuationModeEnum {

    POINTS,
    ROUNDS;

    public static PunctuationModeEnum getEnum(int ordinal) {
        for (PunctuationModeEnum gameTypeEnum : values()) {
            if (gameTypeEnum.ordinal() == ordinal) {
                return gameTypeEnum;
            }
        }

        return null;
    }

}
