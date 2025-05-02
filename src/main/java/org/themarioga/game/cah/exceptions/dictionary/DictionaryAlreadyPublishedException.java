package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryAlreadyPublishedException extends CAHApplicationException {

    public DictionaryAlreadyPublishedException() {
        super(CAHErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
