package org.themarioga.game.cah.enums;

public enum CAHErrorEnum {
    CARD_NOT_FOUND(25L, "Carta no encontrada"),
    CARD_ALREADY_EXISTS(26L, "Carta ya existente"),
    CARD_TEXT_TOO_LONG(27L, "Texto de la carta demasiado largo"),
    CARD_ALREADY_PLAYED(28L, "Carta ya jugada"),
    CARD_ALREADY_VOTED(29L, "Carta ya votada"),
    DICTIONARY_NOT_FOUND(30L, "Diccionario no encontrado"),
    DICTIONARY_NOT_FILLED(31L, "Diccionario no completado"),
    DICTIONARY_ALREADY_EXISTS(32L, "Diccionario ya existe"),
    DICTIONARY_ALREADY_FILLED(33L, "Diccionario ya completado"),
    DICTIONARY_NOT_YOURS(34L, "Diccionario no encontrado"),
    DICTIONARY_COLLAB_ALREADY_EXISTS(35L, "Colaborador ya existente"),
    DICTIONARY_COLLAB_NOT_FOUND(36L, "Colaborador no encontrado"),
    PLAYER_CANNOT_PLAY_CARD(37L, "El jugador no puede jugar esa carta"),
    PLAYER_CANNOT_VOTE_CARD(38L, "El jugador no puede votar"),
    PLAYER_ALREADY_PLAYED_CARD(39L, "El jugador no puede jugar esa carta"),
    PLAYER_ALREADY_VOTED_CARD(40L, "El jugador no puede votar"),
    ROUND_WRONG_STATUS(41L, "Mesa en estado incorrecto");

    final Long errorCode;
    final String errorDesc;

    CAHErrorEnum(Long errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public static CAHErrorEnum getByCode(Long errorCode) {
        for (CAHErrorEnum CAHErrorEnum : values()) {
            if (CAHErrorEnum.getErrorCode().equals(errorCode)) return CAHErrorEnum;
        }

        return null;
    }

    public static CAHErrorEnum getByDesc(String errorDesc) {
        for (CAHErrorEnum CAHErrorEnum : values()) {
            if (CAHErrorEnum.getErrorDesc().equals(errorDesc)) return CAHErrorEnum;
        }

        return null;
    }

}

