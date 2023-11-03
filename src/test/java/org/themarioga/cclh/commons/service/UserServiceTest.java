package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.TableService;
import org.themarioga.cclh.commons.services.intf.UserService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
class UserServiceTest extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    void testCreateOrReactivate() {
        User user = userService.createOrReactivate(4L, "Test");

        Assertions.assertNotNull(user);
        Assertions.assertEquals(4L, user.getId());
        Assertions.assertEquals("Test", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testCreateOrReactivate_Reactivate() {
        User user = userService.createOrReactivate(2L, "Third");

        Assertions.assertNotNull(user);
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertEquals("Third", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

    @Test
    void testCreateOrReactivate_AlreadyExists() {
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createOrReactivate(1L, "Second");

            Assertions.fail();
        });
    }

    @Test
    void testGetById() {
        User user = userService.getById(0L);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(0L, user.getId());
        Assertions.assertEquals("First", user.getName());
        Assertions.assertEquals(true, user.getActive());
    }

}
