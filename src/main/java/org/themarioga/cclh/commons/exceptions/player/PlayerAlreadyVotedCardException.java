package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerAlreadyVotedCardException extends ApplicationException {

    public PlayerAlreadyVotedCardException() {
        super(ErrorEnum.PLAYER_ALREADY_VOTED_CARD);
    }

}
