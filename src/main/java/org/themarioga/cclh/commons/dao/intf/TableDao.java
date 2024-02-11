package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;

public interface TableDao extends InterfaceHibernateDao<Table> {

    PlayedCard getMostVotedCard(Long gameId);

}
