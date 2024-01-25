package org.themarioga.cclh.commons.exceptions.deck;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DeckDoesntExistsException extends ApplicationException {

    public DeckDoesntExistsException() {
        super(ErrorEnum.DECK_NOT_FOUND);
    }

}
