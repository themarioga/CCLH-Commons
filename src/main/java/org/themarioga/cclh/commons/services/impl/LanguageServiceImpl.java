package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.dao.intf.LanguageDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Lang;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.LanguageService;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final Logger logger = LoggerFactory.getLogger(LanguageServiceImpl.class);

    private final LanguageDao languageDao;
    private final ConfigurationService configurationService;

    @Autowired
    public LanguageServiceImpl(LanguageDao languageDao, ConfigurationService configurationService) {
        this.languageDao = languageDao;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Lang getLanguage(String id) {
        logger.debug("Getting language by id: {}", id);

        return languageDao.getLanguage(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Lang getDefaultLanguage() {
        logger.debug("Getting default language");

        return languageDao.getLanguage(configurationService.getConfiguration("default_language"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Lang> getLangs() {
        logger.debug("Getting all languages");

        return languageDao.getLanguages();
    }

}
