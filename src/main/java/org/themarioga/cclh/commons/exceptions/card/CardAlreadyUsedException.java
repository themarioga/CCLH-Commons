package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardAlreadyUsedException extends ApplicationException {

    public CardAlreadyUsedException() {
        super(ErrorEnum.CARD_ALREADY_USED);
    }

}
