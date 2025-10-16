package org.themarioga.engine.cah.enums;

public enum VotationModeEnum {

    DEMOCRACY,
    CLASSIC,
    DICTATORSHIP;

    public static VotationModeEnum getEnum(int ordinal) {
        for (VotationModeEnum votationModeEnum : values()) {
            if (votationModeEnum.ordinal() == ordinal) {
                return votationModeEnum;
            }
        }

        return null;
    }

}
