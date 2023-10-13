package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameNotFilledException extends ApplicationException {

    private final long id;

    public GameNotFilledException(long id) {
        super(ErrorEnum.GAME_NOT_FILLED);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
