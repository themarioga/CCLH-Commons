package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryNotCompletedException extends CAHApplicationException {

    public DictionaryNotCompletedException() {
        super(CAHErrorEnum.DICTIONARY_NOT_FILLED);
    }

}
