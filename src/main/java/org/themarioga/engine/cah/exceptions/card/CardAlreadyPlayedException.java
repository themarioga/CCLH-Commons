package org.themarioga.engine.cah.exceptions.card;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class CardAlreadyPlayedException extends CAHApplicationException {

    public CardAlreadyPlayedException() {
        super(CAHErrorEnum.CARD_ALREADY_PLAYED);
    }

}
