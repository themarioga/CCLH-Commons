package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadySharedException extends CAHApplicationException {

    public DictionaryAlreadySharedException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_SHARED);
    }

}
