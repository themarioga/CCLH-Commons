package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.DeckDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DeckService;

import java.util.Date;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {

    private final Logger logger = LoggerFactory.getLogger(DeckServiceImpl.class);

    private final DeckDao deckDao;
    private final ConfigurationService configurationService;

    @Autowired
    public DeckServiceImpl(DeckDao deckDao, ConfigurationService configurationService) {
        this.deckDao = deckDao;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Deck create(Deck deck) {
        logger.debug("Creating deck: {}", deck);

        deck.setCreationDate(new Date());
        return deckDao.create(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Deck update(Deck deck) {
        logger.debug("Updating deck: {}", deck);

        return deckDao.update(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void delete(Deck deck) {
        logger.debug("Delete deck: {}", deck);

        deckDao.delete(deck);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Deck findOne(long id) {
        logger.debug("Getting deck with ID: {}", id);

        return deckDao.findOne(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public List<Deck> getDeckPaginated(User creator, int firstResult, int maxResults) {
        logger.debug("Getting deck from {} to {}", firstResult, maxResults);

        return deckDao.getDecksPaginated(creator, firstResult, maxResults);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Long getDeckCount(User creator) {
        logger.debug("Get deck count {}", creator);

        return deckDao.getDeckCount(creator);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Deck getDefaultDeck() {
        return deckDao.findOne(Long.parseLong(configurationService.getConfiguration("game_default_dictionary_id")));
    }

}
