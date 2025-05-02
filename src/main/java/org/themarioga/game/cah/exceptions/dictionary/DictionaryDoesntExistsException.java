package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryDoesntExistsException extends CAHApplicationException {

    public DictionaryDoesntExistsException() {
        super(CAHErrorEnum.DICTIONARY_NOT_FOUND);
    }

}
