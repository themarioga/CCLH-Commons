package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyStartedException extends ApplicationException {

    private final long id;

    public GameAlreadyStartedException(long id) {
        super(ErrorEnum.GAME_ALREADY_STARTED);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
