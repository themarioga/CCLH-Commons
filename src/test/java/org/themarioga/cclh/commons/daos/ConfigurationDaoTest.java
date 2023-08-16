package org.themarioga.cclh.commons.daos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.daos.intf.ConfigurationDao;

public class ConfigurationDaoTest extends BaseTest {

    @Autowired
    private ConfigurationDao configurationDao;

    @Test
    public void testGetConfiguration() {
        String value = configurationDao.getConfiguration("cclh_bot_alias");

        Assertions.assertEquals("@cclhbot", value);
    }

}
