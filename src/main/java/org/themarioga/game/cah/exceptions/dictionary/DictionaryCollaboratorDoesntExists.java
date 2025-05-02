package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryCollaboratorDoesntExists extends CAHApplicationException {

    public DictionaryCollaboratorDoesntExists() {
        super(CAHErrorEnum.DICTIONARY_COLLAB_NOT_FOUND);
    }

}
