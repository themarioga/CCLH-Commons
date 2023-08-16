package org.themarioga.cclh.commons.daos;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.daos.intf.UserDao;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
public class UserDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/user/testCreateUser-expected.xml", table = "T_USER")
    public void createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test user");
        user.setActive(true);

        userDao.create(user);
        userDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/user/testUpdateUser-expected.xml", table = "T_USER")
    public void updateUser() {
        User user = userDao.findOne(0L);
        user.setName("Otro nombre");
        user.setActive(false);

        userDao.update(user);
        userDao.getCurrentSession().flush();
    }

    @Test
    public void deleteUser() {
        User user = userDao.findOne(0L);

        userDao.delete(user);
        userDao.getCurrentSession().flush();

        long total = userDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    public void findUser() {
        User user = userDao.findOne(0L);

        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    public void findAllUsers() {
        List<User> users = userDao.findAll();

        Assertions.assertEquals(1, users.size());

        Assertions.assertEquals(0L, users.get(0).getId());
        Assertions.assertEquals("First", users.get(0).getName());
        Assertions.assertEquals(true, users.get(0).getActive());
    }

    @Test
    public void countAllUsers() {
        long total = userDao.countAll();

        Assertions.assertEquals(1, total);
    }

}
