package org.themarioga.engine.cah.services.intf.dictionaries;

import org.themarioga.engine.cah.enums.CardTypeEnum;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;

import java.util.List;
import java.util.UUID;

public interface CardService {

    Card create(Dictionary dictionary, CardTypeEnum type, String text);

    Card changeText(Card card, String newText);

    void delete(Card card);

    Card getCardById(UUID id);

    List<Card> findCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    boolean checkDictionaryCanBePublished(Dictionary dictionary);
}
