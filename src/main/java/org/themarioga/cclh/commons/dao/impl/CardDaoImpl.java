package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

    @Override
    public List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT t FROM Card t where t.dictionary=:dictionary and t.type=:type", Card.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getResultList();
    }

    @Override
    public int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT count(*) FROM Card t where t.dictionary=:dictionary and t.type=:type", Long.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getSingleResultOrNull().intValue();
    }

}
