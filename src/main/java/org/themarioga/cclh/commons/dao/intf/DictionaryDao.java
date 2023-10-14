package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;

import java.util.List;

public interface DictionaryDao extends InterfaceHibernateDao<Dictionary> {

    List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

}
