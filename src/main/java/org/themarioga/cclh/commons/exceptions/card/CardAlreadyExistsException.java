package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardAlreadyExistsException extends ApplicationException {

    public CardAlreadyExistsException() {
        super(ErrorEnum.CARD_ALREADY_EXISTS);
    }

}
