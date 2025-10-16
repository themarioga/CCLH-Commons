package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryDoesntExistsException extends CAHApplicationException {

    public DictionaryDoesntExistsException() {
        super(CAHErrorEnum.DICTIONARY_NOT_FOUND);
    }

}
