package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.CardDao;
import org.themarioga.cclh.models.Card;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

}
