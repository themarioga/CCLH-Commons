package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadyExistsException extends CAHApplicationException {

    public DictionaryAlreadyExistsException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_EXISTS);
    }

}
