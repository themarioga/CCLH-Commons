package org.themarioga.game.cah.exceptions.round;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class RoundWrongStatusException extends CAHApplicationException {

    public RoundWrongStatusException() {
        super(CAHErrorEnum.ROUND_WRONG_STATUS);
    }

}
