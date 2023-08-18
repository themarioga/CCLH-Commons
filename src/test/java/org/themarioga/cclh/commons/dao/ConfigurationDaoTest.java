package org.themarioga.cclh.commons.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.ConfigurationDao;

class ConfigurationDaoTest extends BaseTest {

    @Autowired
    private ConfigurationDao configurationDao;

    @Test
    void testGetConfiguration() {
        String value = configurationDao.getConfiguration("cclh_bot_alias");

        Assertions.assertEquals("@cclhbot", value);
    }

}
