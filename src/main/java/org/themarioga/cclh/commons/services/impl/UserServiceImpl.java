package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.UserService;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Override
    public User create(User user) {
        logger.debug("Creating user: {}", user);

        user.setCreationDate(new Date());
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
