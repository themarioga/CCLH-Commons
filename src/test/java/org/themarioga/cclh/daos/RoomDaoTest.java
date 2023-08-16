package org.themarioga.cclh.daos;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.BaseTest;
import org.themarioga.cclh.daos.intf.RoomDao;
import org.themarioga.cclh.daos.intf.UserDao;
import org.themarioga.cclh.models.Room;
import org.themarioga.cclh.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
public class RoomDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/room/testCreateRoom-expected.xml", table = "T_ROOM")
    public void createRoom() {
        User user = userDao.findOne(0);

        Room room = new Room();
        room.setId(1L);
        room.setName("Test room");
        room.setActive(true);
        room.setOwner(user);

        roomDao.create(room);
        roomDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/room/testUpdateRoom-expected.xml", table = "T_ROOM")
    public void updateRoom() {
        Room room = roomDao.findOne(0L);
        room.setName("Otro nombre");
        room.setActive(false);

        roomDao.update(room);
        roomDao.getCurrentSession().flush();
    }

    @Test
    public void deleteRoom() {
        Room room = roomDao.findOne(0L);

        roomDao.delete(room);
        roomDao.getCurrentSession().flush();

        long total = roomDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    public void findRoom() {
        Room room = roomDao.findOne(0L);

        Assertions.assertEquals(0L, room.getId());
        Assertions.assertEquals("First", room.getName());
        Assertions.assertEquals(true, room.getActive());
    }

    @Test
    public void findAllRooms() {
        List<Room> rooms = roomDao.findAll();

        Assertions.assertEquals(rooms.size(), 1);

        Assertions.assertEquals(0L, rooms.get(0).getId());
        Assertions.assertEquals("First", rooms.get(0).getName());
        Assertions.assertEquals(true, rooms.get(0).getActive());
    }

    @Test
    public void countAllRooms() {
        long total = roomDao.countAll();

        Assertions.assertEquals(1, total);
    }

}
