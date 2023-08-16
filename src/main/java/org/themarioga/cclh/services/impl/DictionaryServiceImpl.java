package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.DictionaryDao;
import org.themarioga.cclh.models.Dictionary;
import org.themarioga.cclh.services.intf.DictionaryService;

public class DictionaryServiceImpl implements DictionaryService {

    private Logger logger = LoggerFactory.getLogger(DictionaryService.class);

    @Autowired
    DictionaryDao dictionaryDao;

    @Override
    public Dictionary create(Dictionary dictionary) {
        logger.debug("Creating dictionary: {}", dictionary);

        return dictionaryDao.create(dictionary);
    }

    @Override
    public Dictionary update(Dictionary dictionary) {
        logger.debug("Updating dictionary: {}", dictionary);

        return dictionaryDao.update(dictionary);
    }

    @Override
    public void delete(Dictionary dictionary) {
        logger.debug("Delete dictionary: {}", dictionary);

        dictionaryDao.delete(dictionary);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete dictionary by ID: {}", id);

        dictionaryDao.deleteById(id);
    }

    @Override
    public Dictionary findOne(long id) {
        logger.debug("Getting dictionary with ID: {}", id);

        return dictionaryDao.findOne(id);
    }
}
