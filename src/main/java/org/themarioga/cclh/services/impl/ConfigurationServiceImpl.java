package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.ConfigurationDao;
import org.themarioga.cclh.services.intf.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {

    private Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public String getConfiguration(String key) {
        logger.debug("Obteniendo configuración por clave: {}", key);

        return configurationDao.getConfiguration(key);
    }

}
