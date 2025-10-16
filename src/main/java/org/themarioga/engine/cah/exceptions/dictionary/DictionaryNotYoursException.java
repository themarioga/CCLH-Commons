package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryNotYoursException extends CAHApplicationException {

    public DictionaryNotYoursException() {
        super(CAHErrorEnum.DICTIONARY_NOT_YOURS);
    }

}
