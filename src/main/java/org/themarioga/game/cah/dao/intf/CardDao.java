package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.commons.dao.InterfaceHibernateDao;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.models.*;

import java.util.List;

public interface CardDao extends InterfaceHibernateDao<Card> {

    boolean checkCardExistsByDictionaryTypeAndText(Dictionary dictionary, CardTypeEnum cardTypeEnum, String text);

    List<Card> findCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    int countCardsByDictionaryAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

}
