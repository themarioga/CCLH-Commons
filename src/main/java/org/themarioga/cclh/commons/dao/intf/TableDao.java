package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Table;
import org.themarioga.cclh.commons.models.VotedCard;

public interface TableDao extends InterfaceHibernateDao<Table> {

    VotedCard getMostVotedCard(long gameId);

}
