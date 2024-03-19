package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.dictionary.DictionaryAlreadyExistsException;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DictionaryService;

import java.util.Date;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    private final DictionaryDao dictionaryDao;
    private final ConfigurationService configurationService;

    @Autowired
    public DictionaryServiceImpl(DictionaryDao dictionaryDao, ConfigurationService configurationService) {
        this.dictionaryDao = dictionaryDao;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Dictionary create(String name, User creator) {
        logger.debug("Creating dictionary: {}", name);

        if (dictionaryDao.countDictionariesByName(name) > 0)
            throw new DictionaryAlreadyExistsException();

        Dictionary dictionary = new Dictionary();
        dictionary.setName(name);
        dictionary.setCreator(creator);
        dictionary.setPublished(false);
        dictionary.setShared(false);
        dictionary.setCreationDate(new Date());
        return dictionaryDao.create(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void setName(Dictionary dictionary, String newName) {
        logger.debug("Setting name {} to dictionary {}", newName, dictionary);

        if (dictionaryDao.countDictionariesByName(newName) > 0)
            throw new DictionaryAlreadyExistsException();

        dictionary.setName(newName);

        dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Dictionary dictionary) {
        logger.debug("Delete dictionary: {}", dictionary);

        dictionaryDao.delete(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Dictionary findOne(long id) {
        logger.debug("Getting dictionary with ID: {}", id);

        return dictionaryDao.findOne(id);
    }

    @Override
    public List<Dictionary> getUserDictionaries(User creator) {
        logger.debug("Getting dictionaries with user: {}", creator);

        return dictionaryDao.getUserDictionaries(creator);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults) {
        logger.debug("Getting dictionary from {} to {}", firstResult, maxResults);

        return dictionaryDao.getDictionariesPaginated(creator, firstResult, maxResults);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Long getDictionaryCount(User creator) {
        logger.debug("Get dictionary count {}", creator);

        return dictionaryDao.getDictionaryCount(creator);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Dictionary getDefaultDictionary() {
        return dictionaryDao.findOne(Long.parseLong(configurationService.getConfiguration("game_default_dictionary_id")));
    }

}
