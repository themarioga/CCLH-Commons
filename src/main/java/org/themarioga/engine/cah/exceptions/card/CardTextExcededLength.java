package org.themarioga.engine.cah.exceptions.card;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class CardTextExcededLength extends CAHApplicationException {

    public CardTextExcededLength() {
        super(CAHErrorEnum.CARD_TEXT_TOO_LONG);
    }

}
