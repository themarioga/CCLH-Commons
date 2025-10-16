package org.themarioga.engine.cah.exceptions.round;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class RoundWrongStatusException extends CAHApplicationException {

    public RoundWrongStatusException() {
        super(CAHErrorEnum.ROUND_WRONG_STATUS);
    }

}
