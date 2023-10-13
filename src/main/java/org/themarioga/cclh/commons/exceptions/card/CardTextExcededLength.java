package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardTextExcededLength extends ApplicationException {

    public CardTextExcededLength() {
        super(ErrorEnum.CARD_TEXT_TOO_LONG);
    }

}
