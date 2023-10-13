package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardDoesntExistsException extends ApplicationException {

    public CardDoesntExistsException() {
        super(ErrorEnum.CARD_NOT_FOUND);
    }

}
