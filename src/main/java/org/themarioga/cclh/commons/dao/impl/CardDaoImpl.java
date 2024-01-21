package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Deck;

import java.util.List;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

    @Override
    public List<Card> findCardsByDictionaryIdAndType(Deck deck, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT t FROM Card t where t.deck=:deck and t.type=:type", Card.class).setParameter("deck", deck).setParameter("type", cardTypeEnum).getResultList();
    }

    @Override
    public int countCardsByDictionaryIdAndType(Deck deck, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT count(*) FROM Card t where t.deck=:deck and t.type=:type", Long.class).setParameter("deck", deck).setParameter("type", cardTypeEnum).getSingleResultOrNull().intValue();
    }

}
