package org.themarioga.cclh.commons.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.daos.AbstractHibernateDao;
import org.themarioga.cclh.commons.daos.intf.GameDao;
import org.themarioga.cclh.commons.models.Game;

@Repository
public class GameDaoImpl extends AbstractHibernateDao<Game> implements GameDao {

    public GameDaoImpl() {
        setClazz(Game.class);
    }

}
