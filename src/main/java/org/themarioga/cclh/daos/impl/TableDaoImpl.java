package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.TableDao;
import org.themarioga.cclh.models.PlayedCard;
import org.themarioga.cclh.models.PlayerVote;
import org.themarioga.cclh.models.Table;

@Repository
public class TableDaoImpl extends AbstractHibernateDao<Table> implements TableDao {

    public TableDaoImpl() {
        setClazz(Table.class);
    }

}
