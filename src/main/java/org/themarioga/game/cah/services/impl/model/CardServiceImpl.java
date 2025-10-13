package org.themarioga.game.cah.services.impl.model;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.game.cah.exceptions.dictionary.DictionaryAlreadyFilledException;
import org.themarioga.game.commons.services.intf.ConfigurationService;
import org.themarioga.game.cah.dao.intf.CardDao;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.cah.exceptions.card.CardAlreadyExistsException;
import org.themarioga.game.cah.exceptions.card.CardTextExcededLength;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.cah.services.intf.model.CardService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    private final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardDao cardDao;
    private final ConfigurationService configurationService;

    @Autowired
    public CardServiceImpl(CardDao cardDao, ConfigurationService configurationService) {
        this.cardDao = cardDao;
        this.configurationService = configurationService;
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
        if (checkCardTextExcededLength(type, text))
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
        if (checkCardTextExcededLength(card.getType(), newText))
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
        if (cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE) < getDictionaryMinWhiteCards())
            return false;

        if (cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK) < getDictionaryMinBlackCards())
            return false;

        return true;
    }

    private int getDictionaryMinWhiteCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_min_whitecards"));
    }

    private int getDictionaryMaxWhiteCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecards"));
    }

    private int getDictionaryWhiteCardMaxLength() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecard_length"));
    }

    private int getDictionaryMinBlackCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_min_blackcards"));
    }

    private int getDictionaryMaxBlackCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcards"));
    }

    private int getDictionaryBlackCardMaxLength() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcard_length"));
    }

    private boolean checkDictionaryAlreadyFilled(Dictionary dictionary, CardTypeEnum type) {
        if (type == CardTypeEnum.WHITE) {
            return cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE) >= getDictionaryMaxWhiteCards();
        } else if (type == CardTypeEnum.BLACK) {
            return cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK) >= getDictionaryMaxBlackCards();
        }

        throw new ApplicationException("Error desconocido");
    }

    private boolean checkCardTextExcededLength(CardTypeEnum type, String text) {
        return (type == CardTypeEnum.WHITE && text.length() > getDictionaryWhiteCardMaxLength()) || (type == CardTypeEnum.BLACK && text.length() > getDictionaryBlackCardMaxLength());
    }

}
