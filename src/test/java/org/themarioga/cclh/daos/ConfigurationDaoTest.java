package org.themarioga.cclh.daos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.BaseTest;
import org.themarioga.cclh.daos.intf.ConfigurationDao;

public class ConfigurationDaoTest extends BaseTest {

    @Autowired
    private ConfigurationDao configurationDao;

    @Test
    public void testGetConfiguration() {
        String value = configurationDao.getConfiguration("cclh_bot_alias");

        Assertions.assertEquals("@cclhbot", value);
    }

}
