package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryNotYoursException extends CAHApplicationException {

    public DictionaryNotYoursException() {
        super(CAHErrorEnum.DICTIONARY_NOT_YOURS);
    }

}
