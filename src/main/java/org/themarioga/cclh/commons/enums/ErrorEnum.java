package org.themarioga.cclh.commons.enums;

public enum ErrorEnum {
    USER_EMPTY(1L, "El usuario no puede ser nulo"),
    USER_ID_EMPTY(2L, "El id de usuario no puede ser nulo"),
    USER_NAME_EMPTY(3L, "El nombre de usuario no puede estar vacío"),
    USER_NOT_FOUND(4L, "El usuario no se ha encontrado"),
    USER_NOT_ACTIVE(5L, "El usuario no está activo"),
    USER_ALREADY_EXISTS(6L, "El usuario ya existe"),
    ROOM_EMPTY(7L, "La sala no puede ser nula"),
    ROOM_ID_EMPTY(8L, "El id de la sala no puede ser nulo"),
    ROOM_NAME_EMPTY(9L, "El nombre de la sala no puede estar vacía"),
    ROOM_NOT_FOUND(10L, "La sala no se ha encontrado"),
    ROOM_NOT_ACTIVE(11L, "La sala no está activa"),
    ROOM_ALREADY_EXISTS(12L, "La sala ya existe"),
    GAME_NOT_FOUND(13L, "Juego no encontrado"),
    GAME_NOT_CONFIGURED(14L, "Juego no configurado"),
    GAME_NOT_FILLED(15L, "Juego no completado"),
    GAME_ALREADY_EXISTS(16L, "Juego ya configurado"),
    GAME_ALREADY_CONFIGURED(17L, "Juego ya configurado"),
    GAME_ALREADY_FILLED(18L, "Juego lleno"),
    GAME_ALREADY_STARTED(19L, "Juego ya iniciado"),
    GAME_CREATOR_CANNOT_LEAVE(20L, "El creador no puede dejar el juego"),
    CARD_NOT_FOUND(21L, "Carta no encontrada"),
    CARD_ALREADY_EXISTS(22L, "Carta ya existente"),
    CARD_TEXT_TOO_LONG(23L, "Texto de la carta demasiado largo"),
    CARD_ALREADY_PLAYED(24L, "Carta ya jugada"),
    CARD_ALREADY_VOTED(25L, "Carta ya votada"),
    DICTIONARY_NOT_FOUND(26L, "Diccionario no encontrado"),
    DICTIONARY_NOT_FILLED(27L, "Diccionario no completado"),
    DICTIONARY_ALREADY_EXISTS(28L, "Diccionario ya existe"),
    DICTIONARY_ALREADY_FILLED(29L, "Diccionario ya completado"),
    PLAYER_NOT_FOUND(30L, "Jugador no encontrado"),
    PLAYER_ALREADY_EXISTS(31L, "Jugador ya existente"),
    PLAYER_ALREADY_VOTED_DELETION(32L, "El jugador ya ha votado"),
    PLAYER_ALREADY_PLAYED_CARD(33L, "El jugador ya ha jugado"),
    PLAYER_ALREADY_VOTED_CARD(34L, "El jugador ya ha votado"),
    PLAYER_CANNOT_VOTE_CARD(35L, "El jugador no puede votar"),
    PLAYER_CANNOT_VOTE_DELETION(36L, "El jugador no puede votar");

    final Long errorCode;
    final String errorDesc;

    ErrorEnum(Long errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public static ErrorEnum getByCode(Long errorCode) {
        for (ErrorEnum errorEnum : values()) {
            if (errorEnum.getErrorCode().equals(errorCode)) return errorEnum;
        }

        return null;
    }

    public static ErrorEnum getByDesc(String errorDesc) {
        for (ErrorEnum errorEnum : values()) {
            if (errorEnum.getErrorDesc().equals(errorDesc)) return errorEnum;
        }

        return null;
    }

}
