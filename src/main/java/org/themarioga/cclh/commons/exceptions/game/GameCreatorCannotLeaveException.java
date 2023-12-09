package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameCreatorCannotLeaveException extends ApplicationException {

    private final long id;

    public GameCreatorCannotLeaveException(long id) {
        super(ErrorEnum.GAME_CREATOR_CANNOT_LEAVE);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
