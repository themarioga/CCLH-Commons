package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryNotCompletedException extends CAHApplicationException {

    public DictionaryNotCompletedException() {
        super(CAHErrorEnum.DICTIONARY_NOT_FILLED);
    }

}
