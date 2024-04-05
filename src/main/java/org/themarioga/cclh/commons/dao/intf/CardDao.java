package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

public interface CardDao extends InterfaceHibernateDao<Card> {

    boolean checkCardExistsByDictionaryTypeAndText(Dictionary dictionary, CardTypeEnum cardTypeEnum, String text);

    List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

}
