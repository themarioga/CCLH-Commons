package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Deck;

import java.util.List;

public interface CardDao extends InterfaceHibernateDao<Card> {

    List<Card> findCardsByDictionaryIdAndType(Deck deck, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryIdAndType(Deck deck, CardTypeEnum cardTypeEnum);

}
