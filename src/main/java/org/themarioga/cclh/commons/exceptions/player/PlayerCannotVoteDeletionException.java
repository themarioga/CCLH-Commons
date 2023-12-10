package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerCannotVoteDeletionException extends ApplicationException {

    public PlayerCannotVoteDeletionException() {
        super(ErrorEnum.PLAYER_CANNOT_VOTE_DELETION);
    }

}
