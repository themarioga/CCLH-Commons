package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.LanguageDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
class UserDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LanguageDao languageDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/user/testCreateUser-expected.xml", table = "T_USER", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Test user");
        user.setActive(true);
        user.setLang(languageDao.getLanguage("es"));

        userDao.create(user);
        getCurrentSession().flush();

        Assertions.assertEquals(2L, user.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/user/testUpdateUser-expected.xml", table = "T_USER", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateUser() {
        User user = userDao.findOne(0L);
        user.setName("Otro nombre");
        user.setActive(false);

        userDao.update(user);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, user.getId());
    }

    @Test
    void deleteUser() {
        User user = userDao.findOne(0L);

        userDao.delete(user);

        long total = userDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    void findUser() {
        User user = userDao.findOne(0L);

        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void findAllUsers() {
        List<User> users = userDao.findAll();

        Assertions.assertEquals(2, users.size());

        Assertions.assertEquals(0L, users.get(0).getId());
        Assertions.assertEquals("First", users.get(0).getName());
        Assertions.assertEquals(true, users.get(0).getActive());
    }

    @Test
    void countAllUsers() {
        long total = userDao.countAll();

        Assertions.assertEquals(2, total);
    }

}
