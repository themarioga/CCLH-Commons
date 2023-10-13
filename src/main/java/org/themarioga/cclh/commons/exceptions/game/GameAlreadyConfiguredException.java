package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyConfiguredException extends ApplicationException {

    private final long id;

    public GameAlreadyConfiguredException(long id) {
        super(ErrorEnum.GAME_ALREADY_CONFIGURED);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
