package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.RoomDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.models.Room;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
class RoomDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/room/testCreateRoom-expected.xml", table = "T_ROOM", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createRoom() {
        Room room = new Room();
        room.setId(2L);
        room.setName("Test room");
        room.setActive(true);

        roomDao.create(room);
        getCurrentSession().flush();

        Assertions.assertEquals(2L, room.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/room/testUpdateRoom-expected.xml", table = "T_ROOM", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateRoom() {
        Room room = roomDao.findOne(0L);
        room.setName("Otro nombre");
        room.setActive(false);

        roomDao.update(room);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, room.getId());
    }

    @Test
    void deleteRoom() {
        Room room = roomDao.findOne(0L);

        roomDao.delete(room);

        long total = roomDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    void findRoom() {
        Room room = roomDao.findOne(0L);

        Assertions.assertEquals(0L, room.getId());
        Assertions.assertEquals("First", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    void findAllRooms() {
        List<Room> rooms = roomDao.findAll();

        Assertions.assertEquals(2, rooms.size());

        Assertions.assertEquals(0L, rooms.get(0).getId());
        Assertions.assertEquals("First", rooms.get(0).getName());
        Assertions.assertEquals(true, rooms.get(0).getActive());
    }

    @Test
    void countAllRooms() {
        long total = roomDao.countAll();

        Assertions.assertEquals(2, total);
    }

}
