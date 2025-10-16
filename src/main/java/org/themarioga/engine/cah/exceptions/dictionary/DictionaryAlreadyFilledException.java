package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadyFilledException extends CAHApplicationException {

    public DictionaryAlreadyFilledException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
