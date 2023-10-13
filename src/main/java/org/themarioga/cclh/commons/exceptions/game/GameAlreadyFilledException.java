package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyFilledException extends ApplicationException {

    private final long id;

    public GameAlreadyFilledException(long id) {
        super(ErrorEnum.GAME_ALREADY_FILLED);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
