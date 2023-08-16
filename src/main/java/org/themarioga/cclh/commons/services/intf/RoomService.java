package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Room;

public interface RoomService {

    Room create(Room room);

    Room update(Room room);

    void delete(Room room);

    void deleteById(long id);

    Room findOne(long id);

}
