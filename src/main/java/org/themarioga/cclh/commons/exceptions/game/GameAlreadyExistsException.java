package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyExistsException extends ApplicationException {

    private final long id;

    public GameAlreadyExistsException(long id) {
        super(ErrorEnum.GAME_ALREADY_EXISTS);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
