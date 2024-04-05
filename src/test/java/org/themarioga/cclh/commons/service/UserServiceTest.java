package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserNotActiveException;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.LanguageService;
import org.themarioga.cclh.commons.services.intf.UserService;

import java.util.List;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
class UserServiceTest extends BaseTest {

    @Autowired
    UserService userService;

    @Autowired
    LanguageService languageService;

    @Test
    void testCreateOrReactivate() {
        User user = userService.createOrReactivate(10L, "Test", languageService.getDefaultLanguage());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(10L, user.getId());
        Assertions.assertEquals("Test", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testCreateOrReactivate_Reactivate() {
        User user = userService.createOrReactivate(2L, "Third", languageService.getDefaultLanguage());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertEquals("Third", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testCreateOrReactivate_AlreadyActive() {
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createOrReactivate(0L, "First", languageService.getDefaultLanguage());

            Assertions.fail();
        });
    }

    @Test
    void testCreateOrReactivate_AlreadyExists() {
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createOrReactivate(1L, "Second", languageService.getDefaultLanguage());

            Assertions.fail();
        });
    }

    @Test
    void testRename() {
        User user = userService.getById(0L);

        userService.rename(user, "Newname");

        Assertions.assertNotNull(user);
        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("Newname", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testSetActive() {
        User user = userService.getById(0L);

        userService.setActive(user, false);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(false, user.getActive());
    }

    @Test
    void testGetById() {
        User user = userService.getById(0L);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testGetById_NonExistant() {
        Assertions.assertThrows(UserDoesntExistsException.class, () -> userService.getById(10L));
    }

    @Test
    void testGetById_NotActive() {
        Assertions.assertThrows(UserNotActiveException.class, () -> userService.getById(2L));
    }

    @Test
    void testGetByUsername() {
        User user = userService.getByUsername("First");

        Assertions.assertNotNull(user);
        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = userService.getAllUsers();

        Assertions.assertEquals(7, userList.size());
    }

}
