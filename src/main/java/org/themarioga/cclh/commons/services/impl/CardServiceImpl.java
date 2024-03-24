package org.themarioga.cclh.commons.services.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.card.CardAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.dictionary.DictionaryAlreadyFilledException;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.CardService;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;

import java.util.Date;
import java.util.List;

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

        if (cardDao.checkCardExistsByDictionaryTypeAndText(dictionary, type, text))
            throw new CardAlreadyExistsException();

        checkDictionaryAlreadyFilled(dictionary, type);

        Card card = new Card();
        card.setDictionary(dictionary);
        card.setText(text);
        card.setType(type);
        card.setCreationDate(new Date());

        return cardDao.create(card);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void changeText(Card card, String newText) {
        logger.debug("Updating card text: {} {}", card, newText);

        if (cardDao.checkCardExistsByDictionaryTypeAndText(card.getDictionary(), card.getType(), newText))
            throw new CardAlreadyExistsException();

        card.setText(newText);

        cardDao.update(card);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Card card) {
        logger.debug("Delete card: {}", card);

        cardDao.delete(card);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Card getCardById(long id) {
        logger.debug("Getting card with ID: {}", id);

        return cardDao.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Getting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return cardDao.findCardsByDictionaryIdAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Counting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return cardDao.countCardsByDictionaryIdAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean checkDictionaryCanBePublished(Dictionary dictionary) {
        if (cardDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.WHITE) < getDictionaryMinWhiteCards())
            return false;

        if (cardDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK) < getDictionaryMinBlackCards())
            return false;

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryMinWhiteCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_min_whitecards"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryMaxWhiteCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecards"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryWhiteCardMaxLength() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecard_length"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryMinBlackCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_min_blackcards"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryMaxBlackCards() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcards"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryBlackCardMaxLength() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcard_length"));
    }

    private void checkDictionaryAlreadyFilled(Dictionary dictionary, CardTypeEnum type) {
        if (type == CardTypeEnum.WHITE) {
            if (cardDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.WHITE) >= getDictionaryMaxWhiteCards()) {
                throw new DictionaryAlreadyFilledException();
            }
        } else if (type == CardTypeEnum.BLACK) {
            if (cardDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK) >= getDictionaryMaxBlackCards()) {
                throw new DictionaryAlreadyFilledException();
            }
        }
    }

}
