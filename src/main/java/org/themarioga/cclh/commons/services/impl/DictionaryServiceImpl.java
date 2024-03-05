package org.themarioga.cclh.commons.services.impl;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
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
    public Dictionary create(Dictionary dictionary) {
        logger.debug("Creating dictionary: {}", dictionary);

        dictionary.setCreationDate(new Date());
        return dictionaryDao.create(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Dictionary update(Dictionary dictionary) {
        logger.debug("Updating dictionary: {}", dictionary);

        return dictionaryDao.update(dictionary);
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
