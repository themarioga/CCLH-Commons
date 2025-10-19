package org.themarioga.engine.cah.enums;

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
    DICTIONARY_ALREADY_SHARED(34L, "Diccionario ya compartido"),
    DICTIONARY_NOT_YOURS(35L, "Diccionario no encontrado"),
    DICTIONARY_COLLAB_ALREADY_EXISTS(36L, "Colaborador ya existente"),
    DICTIONARY_COLLAB_NOT_FOUND(37L, "Colaborador no encontrado"),
    DICTIONARY_COLLAB_CREATOR_CANT_BE_ALTERED(38L, "Colaborador es creador"),
    PLAYER_CANNOT_DRAW_CARD(39L, "El jugador no puede sacar esa carta"),
    PLAYER_CANNOT_PLAY_CARD(40L, "El jugador no puede jugar esa carta"),
    PLAYER_CANNOT_VOTE_CARD(41L, "El jugador no puede votar"),
    PLAYER_ALREADY_PLAYED_CARD(42L, "El jugador no puede jugar esa carta"),
    PLAYER_ALREADY_VOTED_CARD(43L, "El jugador no puede votar"),
    ROUND_WRONG_STATUS(44L, "Mesa en estado incorrecto"),
	ROUND_PRESIDENT_CANNOT_PLAY_CARD(45L, "El presidente de la ronda no puede jugar una carta");

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

