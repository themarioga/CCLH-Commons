package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DictionaryService;

import java.util.Date;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Autowired
    DictionaryDao dictionaryDao;

    @Autowired
    ConfigurationService configurationService;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Dictionary create(Dictionary dictionary) {
        logger.debug("Creating dictionary: {}", dictionary);

        dictionary.setCreationDate(new Date());
        return dictionaryDao.create(dictionary);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Dictionary update(Dictionary dictionary) {
        logger.debug("Updating dictionary: {}", dictionary);

        return dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void delete(Dictionary dictionary) {
        logger.debug("Delete dictionary: {}", dictionary);

        dictionaryDao.delete(dictionary);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void deleteById(long id) {
        logger.debug("Delete dictionary by ID: {}", id);

        dictionaryDao.deleteById(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Dictionary findOne(long id) {
        logger.debug("Getting dictionary with ID: {}", id);

        return dictionaryDao.findOne(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Getting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return dictionaryDao.findCardsByDictionaryIdAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public long countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        logger.debug("Counting cards by dictionary {} and type {}", dictionary, cardTypeEnum);

        return dictionaryDao.countCardsByDictionaryIdAndType(dictionary, cardTypeEnum);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Dictionary getDefaultDictionary() {
        return dictionaryDao.findOne(Long.parseLong(configurationService.getConfiguration("game_default_dictionary_id")));
    }

}
