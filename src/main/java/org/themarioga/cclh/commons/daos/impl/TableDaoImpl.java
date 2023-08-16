package org.themarioga.cclh.commons.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.daos.AbstractHibernateDao;
import org.themarioga.cclh.commons.daos.intf.TableDao;
import org.themarioga.cclh.commons.models.Table;

@Repository
public class TableDaoImpl extends AbstractHibernateDao<Table> implements TableDao {

    public TableDaoImpl() {
        setClazz(Table.class);
    }

}
