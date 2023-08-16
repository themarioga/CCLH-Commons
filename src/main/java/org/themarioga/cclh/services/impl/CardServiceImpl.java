package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.CardDao;
import org.themarioga.cclh.models.Card;
import org.themarioga.cclh.services.intf.CardService;

public class CardServiceImpl implements CardService {

    private Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    CardDao cardDao;

    @Override
    public Card create(Card card) {
        logger.debug("Creating card: {}", card);

        return cardDao.create(card);
    }

    @Override
    public Card update(Card card) {
        logger.debug("Updating card: {}", card);

        return cardDao.update(card);
    }

    @Override
    public void delete(Card card) {
        logger.debug("Delete card: {}", card);

        cardDao.delete(card);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete card by ID: {}", id);

        cardDao.deleteById(id);
    }

    @Override
    public Card findOne(long id) {
        logger.debug("Getting card with ID: {}", id);

        return cardDao.findOne(id);
    }
}
