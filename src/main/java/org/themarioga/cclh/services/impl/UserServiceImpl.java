package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.UserDao;
import org.themarioga.cclh.models.User;
import org.themarioga.cclh.services.intf.UserService;

public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDao userDao;

    @Override
    public User create(User user) {
        logger.debug("Creating user: {}", user);

        return userDao.create(user);
    }

    @Override
    public User update(User user) {
        logger.debug("Updating user: {}", user);

        return userDao.update(user);
    }

    @Override
    public void delete(User user) {
        logger.debug("Delete user: {}", user);

        userDao.delete(user);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete user by ID: {}", id);

        userDao.deleteById(id);
    }

    @Override
    public User findOne(long id) {
        logger.debug("Getting user with ID: {}", id);

        return userDao.findOne(id);
    }

}
