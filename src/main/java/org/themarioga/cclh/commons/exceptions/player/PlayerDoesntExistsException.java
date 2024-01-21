package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerDoesntExistsException extends ApplicationException {

    public PlayerDoesntExistsException() {
        super(ErrorEnum.PLAYER_NOT_FOUND);
    }

}
