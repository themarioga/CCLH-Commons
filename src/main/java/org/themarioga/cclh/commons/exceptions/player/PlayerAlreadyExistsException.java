package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerAlreadyExistsException extends ApplicationException {

    public PlayerAlreadyExistsException() {
        super(ErrorEnum.PLAYER_ALREADY_EXISTS);
    }

}
