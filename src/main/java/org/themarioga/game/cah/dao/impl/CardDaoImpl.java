package org.themarioga.game.cah.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.game.commons.dao.AbstractHibernateDao;
import org.themarioga.game.cah.dao.intf.CardDao;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.models.*;

import java.util.List;

@Repository
public class CardDaoImpl extends AbstractHibernateDao<Card> implements CardDao {

    public CardDaoImpl() {
        setClazz(Card.class);
    }

    @Override
    public boolean checkCardExistsByDictionaryTypeAndText(Dictionary dictionary, CardTypeEnum cardTypeEnum, String text) {
        return getCurrentSession().createQuery("SELECT count(c) FROM Card c WHERE c.dictionary=:dictionary and c.type=:type and c.text LIKE :text", Long.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).setParameter("text", text).getSingleResultOrNull() > 0;
    }

    @Override
    public List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT c FROM Card c where c.dictionary=:dictionary and c.type=:type", Card.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getResultList();
    }

    @Override
    public int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT count(c) FROM Card c where c.dictionary=:dictionary and c.type=:type", Long.class).setParameter("dictionary", dictionary).setParameter("type", cardTypeEnum).getSingleResultOrNull().intValue();
    }

}
