package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerCannotVoteCardException extends ApplicationException {

    public PlayerCannotVoteCardException() {
        super(ErrorEnum.PLAYER_CANNOT_VOTE_CARD);
    }

}
