package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyConfiguredException extends ApplicationException {

    public GameAlreadyConfiguredException() {
        super(ErrorEnum.GAME_ALREADY_CONFIGURED);
    }

}
