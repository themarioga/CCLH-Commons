package org.themarioga.game.cah.exceptions.game;

import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;

public class GameAlreadyFilledException extends ApplicationException {

    public GameAlreadyFilledException() {
        super(ErrorEnum.GAME_ALREADY_FILLED);
    }

}
