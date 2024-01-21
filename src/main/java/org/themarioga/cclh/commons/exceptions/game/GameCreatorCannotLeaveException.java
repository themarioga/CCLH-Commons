package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameCreatorCannotLeaveException extends ApplicationException {

    public GameCreatorCannotLeaveException() {
        super(ErrorEnum.GAME_CREATOR_CANNOT_LEAVE);
    }

}
