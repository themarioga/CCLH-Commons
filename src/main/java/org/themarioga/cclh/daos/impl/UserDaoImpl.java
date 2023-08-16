package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.UserDao;
import org.themarioga.cclh.models.User;

@Repository
public class UserDaoImpl extends AbstractHibernateDao<User> implements UserDao {

    public UserDaoImpl() {
        setClazz(User.class);
    }

}
