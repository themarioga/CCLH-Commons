package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Room;

import java.util.List;

public interface RoomService {

    Room createOrReactivate(long id, String name);

    Room setActive(Room room, boolean active);

    Room getById(long id);

    List<Room> getAllRooms();

}
