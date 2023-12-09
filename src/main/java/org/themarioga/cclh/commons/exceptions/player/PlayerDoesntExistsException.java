package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerDoesntExistsException extends ApplicationException {

    private final Long userId;

    public PlayerDoesntExistsException(long userId) {
        super(ErrorEnum.PLAYER_NOT_FOUND);

        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
