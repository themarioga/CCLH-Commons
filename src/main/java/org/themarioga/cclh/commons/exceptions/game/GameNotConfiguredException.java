package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameNotConfiguredException extends ApplicationException {

    private final long id;

    public GameNotConfiguredException(long id) {
        super(ErrorEnum.GAME_NOT_CONFIGURED);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
