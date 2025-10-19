package org.themarioga.engine.cah.exceptions.round;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class RoundPresidentCannotPlayCardException extends CAHApplicationException {

    public RoundPresidentCannotPlayCardException() {
        super(CAHErrorEnum.ROUND_PRESIDENT_CANNOT_PLAY_CARD);
    }

}
