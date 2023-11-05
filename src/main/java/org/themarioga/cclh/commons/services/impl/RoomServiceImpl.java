package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.RoomDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.room.RoomNotActiveException;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.services.intf.RoomService;
import org.themarioga.cclh.commons.services.intf.UserService;
import org.themarioga.cclh.commons.util.Assert;

import java.util.Date;

@Service
public class RoomServiceImpl implements RoomService {

    private final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomDao roomDao;
    private final UserService userService;

    @Autowired
    public RoomServiceImpl(RoomDao roomDao, UserService userService) {
        this.roomDao = roomDao;
        this.userService = userService;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Room createOrReactivate(long id, String name, long ownerId) {
        logger.debug("Creating or reactivating room: {} ({})", id, name);

        Assert.assertNotNull(id, ErrorEnum.ROOM_ID_EMPTY);
        Assert.assertNotEmpty(name, ErrorEnum.ROOM_NAME_EMPTY);

        Room roomFromBd = roomDao.findOne(id);
        if (roomFromBd == null) {
            Room room = new Room();
            room.setId(id);
            room.setName(name);
            room.setActive(true);
            room.setOwner(userService.getById(ownerId));
            room.setCreationDate(new Date());

            return roomDao.create(room);
        } else {
            if (Boolean.FALSE.equals(roomFromBd.getActive())) {
                roomFromBd.setName(name);
                roomFromBd.setActive(true);
                roomFromBd.setOwner(userService.getById(ownerId));
                return roomDao.update(roomFromBd);
            } else {
                return roomFromBd;
            }
        }
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Room getById(long id) {
        logger.debug("Getting room with ID: {}", id);

        Room room = roomDao.findOne(id);
        if (room == null) {
            logger.error("Error getting room with id {}: Doesn't exists.", id);
            throw new RoomDoesntExistsException(id);
        }
        if (Boolean.FALSE.equals(room.getActive())) {
            logger.error("Error getting room with id {}: Not active.", id);
            throw new RoomNotActiveException(id);
        }

        return room;
    }

}
