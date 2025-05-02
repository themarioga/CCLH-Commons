package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadyFilledException extends CAHApplicationException {

    public DictionaryAlreadyFilledException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
