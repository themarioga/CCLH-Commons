package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.dao.intf.RoomDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.room.RoomNotActiveException;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.services.intf.RoomService;
import org.themarioga.cclh.commons.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomDao roomDao;

    @Autowired
    public RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Room createOrReactivate(long id, String name) {
        logger.debug("Creating or reactivating room: {} ({})", id, name);

        Assert.assertNotNull(id, ErrorEnum.ROOM_ID_EMPTY);
        Assert.assertNotEmpty(name, ErrorEnum.ROOM_NAME_EMPTY);

        Room roomFromBd = roomDao.findOne(id);
        if (roomFromBd == null) {
            Room room = new Room();
            room.setId(id);
            room.setName(name);
            room.setActive(true);
            room.setCreationDate(new Date());

            return roomDao.create(room);
        } else {
            if (Boolean.FALSE.equals(roomFromBd.getActive())) {
                roomFromBd.setName(name);
                roomFromBd.setActive(true);
                return roomDao.update(roomFromBd);
            } else {
                roomFromBd.setName(name);
                return roomDao.update(roomFromBd);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Room setActive(Room room, boolean active) {
        logger.debug("Activating/Deactivating room with ID {} to {}", room.getId(), active);

        room.setActive(active);

        return roomDao.update(room);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Room getById(long id) {
        logger.debug("Getting room with ID: {}", id);

        Room room = roomDao.findOne(id);
        if (room == null) {
            logger.error("Error getting room with id {}: Doesn't exists.", id);
            throw new RoomDoesntExistsException();
        }
        if (Boolean.FALSE.equals(room.getActive())) {
            logger.error("Error getting room with id {}: Not active.", id);
            throw new RoomNotActiveException();
        }

        return room;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Room> getAllRooms() {
        logger.debug("Getting all rooms");

        return roomDao.findAll();
    }

}
