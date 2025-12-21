package org.themarioga.engine.cah.dao.intf.game;

import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.commons.dao.InterfaceHibernateDao;

public interface GameDao extends org.themarioga.engine.commons.dao.intf.GameDao<Game>, InterfaceHibernateDao<Game> {

    void transferCardsFromDictionaryToDeck(Game game);

}
