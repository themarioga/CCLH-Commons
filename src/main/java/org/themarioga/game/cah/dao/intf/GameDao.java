package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.commons.dao.InterfaceHibernateDao;
import org.themarioga.game.cah.models.*;

public interface GameDao extends org.themarioga.game.commons.dao.intf.GameDao, InterfaceHibernateDao<Game> {

    void transferCardsFromDictionaryToDeck(Game game);

}
