package org.themarioga.cclh.commons.services.intf;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;

import java.util.List;

public interface CardService {

    Card create(Dictionary dictionary, CardTypeEnum type, String text);

    void changeText(Card card, String newText);

    void delete(Card card);

    Card getCardById(long id);

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
