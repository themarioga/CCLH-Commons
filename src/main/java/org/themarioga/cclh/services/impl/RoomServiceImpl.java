package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.RoomDao;
import org.themarioga.cclh.models.Room;
import org.themarioga.cclh.services.intf.RoomService;

public class RoomServiceImpl implements RoomService {

    private Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    RoomDao roomDao;

    @Override
    public Room create(Room room) {
        logger.debug("Creating room: {}", room);

        return roomDao.create(room);
    }

    @Override
    public Room update(Room room) {
        logger.debug("Updating room: {}", room);

        return roomDao.update(room);
    }

    @Override
    public void delete(Room room) {
        logger.debug("Delete room: {}", room);

        roomDao.delete(room);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete room by ID: {}", id);

        roomDao.deleteById(id);
    }

    @Override
    public Room findOne(long id) {
        logger.debug("Getting room with ID: {}", id);

        return roomDao.findOne(id);
    }

}
