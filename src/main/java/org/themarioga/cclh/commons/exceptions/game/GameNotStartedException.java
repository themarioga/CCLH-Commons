package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameNotStartedException extends ApplicationException {

    public GameNotStartedException() {
        super(ErrorEnum.GAME_NOT_CONFIGURED);
    }

}
