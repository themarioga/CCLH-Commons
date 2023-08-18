package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.services.intf.CardService;

import java.util.Date;

@Service
public class CardServiceImpl implements CardService {

    private final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    CardDao cardDao;

    @Override
    public Card create(Card card) {
        logger.debug("Creating card: {}", card);

        card.setCreationDate(new Date());
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
