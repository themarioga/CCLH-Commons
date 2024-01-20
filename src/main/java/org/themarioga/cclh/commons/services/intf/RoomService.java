package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Room;

public interface RoomService {

    Room createOrReactivate(long id, String name);

    Room getById(long id);

}
