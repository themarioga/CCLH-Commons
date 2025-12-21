package org.themarioga.engine.cah.services.impl.dictionaries;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.engine.cah.config.DictionariesConfig;
import org.themarioga.engine.cah.exceptions.dictionary.DictionaryAlreadyFilledException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.dao.intf.dictionaries.CardDao;
import org.themarioga.engine.cah.enums.CardTypeEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;
import org.themarioga.engine.cah.exceptions.card.CardAlreadyExistsException;
import org.themarioga.engine.cah.exceptions.card.CardTextExcededLength;
import org.themarioga.engine.cah.services.intf.dictionaries.CardService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    private final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardDao cardDao;

    @Autowired
    public CardServiceImpl(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Card create(Dictionary dictionary, CardTypeEnum type, String text) {
        logger.debug("Creating card with dictionary: {}, text: {}, type: {}", dictionary, text, type);

        // Check if card with that text and type already exists
        if (cardDao.checkCardExistsByDictionaryTypeAndText(dictionary, type, text))
            throw new CardAlreadyExistsException();

        // Check if the dictionary is already filled
        if (checkDictionaryAlreadyFilled(dictionary, type))
            throw new DictionaryAlreadyFilledException();

        // Check if text limits are exceeded
        if (checkCardTextBadLength(type, text))
            throw new CardTextExcededLength();

        // Create the card
        Card card = new Card();
        card.setDictionary(dictionary);
        card.setText(text);
        card.setType(type);
        card.setCreationDate(new Date());

        return cardDao.createOrUpdate(card);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Card changeText(Card card, String newText) {
        logger.debug("Updating card text: {} {}", card, newText);

        // Check if card with that text already exists
        if (cardDao.checkCardExistsByDictionaryTypeAndText(card.getDictionary(), card.getType(), newText))
            throw new CardAlreadyExistsException();

        // Check if text limits are exceeded
        if (checkCardTextBadLength(card.getType(), newText))
            throw new CardTextExcededLength();

        // Change card text
        card.setText(newText);

        return cardDao.createOrUpdate(card);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Card card) {
        logger.debug("Delete card: {}", card);

        cardDao.delete(card);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Card getCardById(UUID id) {
        logger.debug("Getting card with ID: {}", id);

        return cardDao.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Card> findCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Getting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return cardDao.findCardsByDictionaryAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int countCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Counting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return cardDao.countCardsByDictionaryAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean checkDictionaryCanBePublished(Dictionary dictionary) {
        if (cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE) < DictionariesConfig.MIN_NUMBER_OF_WHITE_CARDS)
            return false;

        if (cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK) < DictionariesConfig.MIN_NUMBER_OF_BLACK_CARDS)
            return false;

        return true;
    }

    private boolean checkDictionaryAlreadyFilled(Dictionary dictionary, CardTypeEnum type) {
        if (type == CardTypeEnum.WHITE) {
            return cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE) >= DictionariesConfig.MAX_NUMBER_OF_WHITE_CARDS;
        } else if (type == CardTypeEnum.BLACK) {
            return cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK) >= DictionariesConfig.MAX_NUMBER_OF_BLACK_CARDS;
        }

        throw new ApplicationException("Error desconocido");
    }

    private boolean checkCardTextBadLength(CardTypeEnum type, String text) {
        return (type == CardTypeEnum.WHITE && (text.length() < DictionariesConfig.MIN_WHITE_CARD_LENGTH || text.length() > DictionariesConfig.MAX_WHITE_CARD_LENGTH))
		        || (type == CardTypeEnum.BLACK && (text.length() < DictionariesConfig.MIN_BLACK_CARD_LENGTH || text.length() > DictionariesConfig.MAX_BLACK_CARD_LENGTH));
    }

}
