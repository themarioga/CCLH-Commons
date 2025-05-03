package org.themarioga.game.cah.services.intf;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.cah.models.Card;
import org.themarioga.game.cah.models.Dictionary;

import java.util.List;
import java.util.UUID;

public interface CardService {

    Card create(Dictionary dictionary, CardTypeEnum type, String text);

    void changeText(Card card, String newText);

    void delete(Card card);

    Card getCardById(UUID id);

    List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int getDictionaryMinWhiteCards();

    int getDictionaryMaxWhiteCards();

    int getDictionaryWhiteCardMaxLength();

    int getDictionaryMinBlackCards();

    int getDictionaryMaxBlackCards();

    int getDictionaryBlackCardMaxLength();

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    boolean checkDictionaryCanBePublished(Dictionary dictionary);
}
