package org.themarioga.cclh.commons.exceptions.deck;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DeckAlreadyExistsException extends ApplicationException {

    public DeckAlreadyExistsException() {
        super(ErrorEnum.DECK_ALREADY_EXISTS);
    }

}
