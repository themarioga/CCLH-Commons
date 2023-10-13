package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Game;

public interface GameDao extends InterfaceHibernateDao<Game> {

    Game getByRoomId(long roomId);

}
