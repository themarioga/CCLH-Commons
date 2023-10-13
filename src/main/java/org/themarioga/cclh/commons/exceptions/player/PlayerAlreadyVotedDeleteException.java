package org.themarioga.cclh.commons.exceptions.player;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class PlayerAlreadyVotedDeleteException extends ApplicationException {

    public PlayerAlreadyVotedDeleteException() {
        super(ErrorEnum.PLAYER_ALREADY_VOTED);
    }

}
