package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerCannotPlayCardException extends ApplicationException {

    public PlayerCannotPlayCardException() {
        super(ErrorEnum.PLAYER_CANNOT_PLAY_CARD);
    }

}
