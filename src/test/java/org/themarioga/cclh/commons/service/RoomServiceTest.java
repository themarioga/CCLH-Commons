package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.exceptions.room.RoomAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.room.RoomNotActiveException;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserNotActiveException;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.RoomService;
import org.themarioga.cclh.commons.services.intf.UserService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
class RoomServiceTest extends BaseTest {

    @Autowired
    RoomService roomService;

    @Test
    void testCreateOrReactivate() {
        Room room = roomService.createOrReactivate(5L, "Test", 0L);

        Assertions.assertNotNull(room);
        Assertions.assertEquals(5L, room.getId());
        Assertions.assertEquals("Test", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    void testCreateOrReactivate_Reactivate() {
        Room room = roomService.createOrReactivate(3L, "Fourth", 0L);

        Assertions.assertNotNull(room);
        Assertions.assertEquals(3L, room.getId());
        Assertions.assertEquals("Fourth", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    void testCreateOrReactivate_AlreadyActive() {
        Room room = roomService.createOrReactivate(0L, "First", 0L);

        Assertions.assertNotNull(room);
        Assertions.assertEquals(0L, room.getId());
        Assertions.assertEquals("First", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    void testGetById() {
        Room room = roomService.getById(0L);

        Assertions.assertNotNull(room);
        Assertions.assertEquals(0L, room.getId());
        Assertions.assertEquals("First", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    void testGetById_NonExistant() {
        Assertions.assertThrows(RoomDoesntExistsException.class, () -> roomService.getById(10L));
    }

    @Test
    void testGetById_NotActive() {
        Assertions.assertThrows(RoomNotActiveException.class, () -> roomService.getById(2L));
    }

}
