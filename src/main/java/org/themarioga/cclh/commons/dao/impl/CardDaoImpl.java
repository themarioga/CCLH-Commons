package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

//    @Override
//    public List<PlayerDeckCard> getCardsForPlayerDeck(long playerId, long dictionaryId, CardTypeEnum cardTypeEnum, int start, int size) {
//        return getCurrentSession()
//                .createNativeQuery("SELECT :player_id as player_id, id as card_id FROM T_CARD WHERE dictionary_id=:dictionary_id AND type=:type", PlayerDeckCard.class)
//                .setParameter("player_id", playerId)
//                .setParameter("dictionary_id", dictionaryId)
//                .setParameter("type", cardTypeEnum)
//                .setFirstResult(start)
//                .setMaxResults(size)
//                .getResultList();
//    }

    @Override
    public List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT t FROM Card t where t.dictionary=:dictionary and t.type=:type", Card.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getResultList();
    }

    @Override
    public int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT count(*) FROM Card t where t.dictionary=:dictionary and t.type=:type", Long.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getSingleResultOrNull().intValue();
    }

}
