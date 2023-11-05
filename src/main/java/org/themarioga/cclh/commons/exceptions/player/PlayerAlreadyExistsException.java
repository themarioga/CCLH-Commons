package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerAlreadyExistsException extends ApplicationException {

    private final long userId;

    public PlayerAlreadyExistsException(long userId) {
        super(ErrorEnum.PLAYER_ALREADY_EXISTS);

        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
