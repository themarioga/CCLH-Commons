package org.themarioga.cclh.commons.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.daos.AbstractHibernateDao;
import org.themarioga.cclh.commons.daos.intf.CardDao;
import org.themarioga.cclh.commons.models.Card;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

}
