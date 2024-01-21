package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.DeckDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DeckService;

import java.util.Date;

@Service
public class DeckServiceImpl implements DeckService {

    private final Logger logger = LoggerFactory.getLogger(DeckServiceImpl.class);

    @Autowired
    DeckDao deckDao;

    @Autowired
    ConfigurationService configurationService;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Deck create(Deck deck) {
        logger.debug("Creating dictionary: {}", deck);

        deck.setCreationDate(new Date());
        return deckDao.create(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Deck update(Deck deck) {
        logger.debug("Updating dictionary: {}", deck);

        return deckDao.update(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void delete(Deck deck) {
        logger.debug("Delete dictionary: {}", deck);

        deckDao.delete(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void deleteById(long id) {
        logger.debug("Delete dictionary by ID: {}", id);

        deckDao.deleteById(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Deck findOne(long id) {
        logger.debug("Getting dictionary with ID: {}", id);

        return deckDao.findOne(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Deck getDefaultDictionary() {
        return deckDao.findOne(Long.parseLong(configurationService.getConfiguration("game_default_dictionary_id")));
    }

}
