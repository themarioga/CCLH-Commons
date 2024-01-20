package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.models.User;

public interface GameDao extends InterfaceHibernateDao<Game> {

    Game getByRoom(Room room);

    Game getByCreator(User creator);

    Long countByRoom(Room room);

    Long countByCreator(User creator);

}
