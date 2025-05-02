package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadyExistsException extends CAHApplicationException {

    public DictionaryAlreadyExistsException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_EXISTS);
    }

}
