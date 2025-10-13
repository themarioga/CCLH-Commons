package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.models.Card;
import org.themarioga.game.cah.models.Dictionary;

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
