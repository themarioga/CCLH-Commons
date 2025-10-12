package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadySharedException extends CAHApplicationException {

    public DictionaryAlreadySharedException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_SHARED);
    }

}
