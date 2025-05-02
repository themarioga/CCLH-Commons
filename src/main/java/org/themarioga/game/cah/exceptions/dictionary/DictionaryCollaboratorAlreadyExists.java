package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryCollaboratorAlreadyExists extends CAHApplicationException {

    public DictionaryCollaboratorAlreadyExists() {
        super(CAHErrorEnum.DICTIONARY_COLLAB_ALREADY_EXISTS);
    }

}
