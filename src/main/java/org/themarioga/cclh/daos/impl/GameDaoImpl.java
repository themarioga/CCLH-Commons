package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.GameDao;
import org.themarioga.cclh.models.Card;
import org.themarioga.cclh.models.Game;

@Repository
public class GameDaoImpl extends AbstractHibernateDao<Game> implements GameDao {

    public GameDaoImpl() {
        setClazz(Game.class);
    }

}
