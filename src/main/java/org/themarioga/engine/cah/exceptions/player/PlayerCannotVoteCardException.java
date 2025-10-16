package org.themarioga.engine.cah.exceptions.player;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class PlayerCannotVoteCardException extends CAHApplicationException {

    public PlayerCannotVoteCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_VOTE_CARD);
    }

}
