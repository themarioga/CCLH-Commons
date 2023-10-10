package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserDoesntExistsException;
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

        try {
            user.setCreationDate(new Date());
            return userDao.create(user);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    public User update(User user) {
        logger.debug("Updating user: {}", user);

        if (findOne(user.getId()) == null) throw new UserDoesntExistsException();

        return userDao.update(user);
    }

    @Override
    public void delete(User user) {
        logger.debug("Delete user: {}", user);

        if (findOne(user.getId()) == null) throw new UserDoesntExistsException();

        userDao.delete(user);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete user by ID: {}", id);

        if (findOne(id) == null) throw new UserDoesntExistsException();

        userDao.deleteById(id);
    }

    @Override
    public User findOne(long id) {
        logger.debug("Getting user with ID: {}", id);

        return userDao.findOne(id);
    }

}
