package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.ConfigurationDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

    private final ConfigurationDao configurationDao;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public String getConfiguration(String key) {
        logger.debug("Obteniendo configuración por clave: {}", key);

        return configurationDao.getConfiguration(key);
    }

}
