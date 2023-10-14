package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;

public interface GameDao extends InterfaceHibernateDao<Game> {

    Game getByRoomId(Room room);

}
