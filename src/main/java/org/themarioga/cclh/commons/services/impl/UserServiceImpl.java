package org.themarioga.cclh.commons.services.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserNotActiveException;
import org.themarioga.cclh.commons.models.Lang;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.LanguageService;
import org.themarioga.cclh.commons.services.intf.UserService;
import org.themarioga.cclh.commons.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public User createOrReactivate(long id, String name, Lang language) {
        logger.debug("Creating or reactivating user: {} ({})", id, name);

        Assert.assertNotNull(id, ErrorEnum.USER_ID_EMPTY);
        Assert.assertNotEmpty(name, ErrorEnum.USER_NAME_EMPTY);

        User userFromBd = userDao.findOne(id);
        if (userFromBd == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setActive(true);
            user.setLang(language);
            user.setCreationDate(new Date());
            return userDao.create(user);
        } else {
            if (Boolean.FALSE.equals(userFromBd.getActive())) {
                userFromBd.setName(name);
                userFromBd.setActive(true);
                return userDao.update(userFromBd);
            } else {
                logger.error("Error trying to create user {} ({}): Already exists", id, name);
                throw new UserAlreadyExistsException();
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public User rename(User user, String newName) {
        logger.debug("Renaming user with ID {} to {}", user.getId(), newName);

        user.setName(newName);

        return userDao.update(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public User setActive(User user, boolean active) {
        logger.debug("Activating/Deactivating user with ID {} to {}", user.getId(), active);

        user.setActive(active);

        return userDao.update(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public User setLanguage(User user, Lang language) {
        logger.debug("Changing lang to user with ID {} to {}", user.getId(), language);

        user.setLang(language);

        return userDao.update(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public User getById(long id) {
        logger.debug("Getting user with ID: {}", id);

        User user = userDao.findOne(id);
        if (user == null) {
            logger.error("Error getting user with id {}: Doesn't exists.", id);
            throw new UserDoesntExistsException();
        }
        if (Boolean.FALSE.equals(user.getActive())) {
            logger.error("Error getting user with id {}: Not active.", id);
            throw new UserNotActiveException();
        }

        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public User getByUsername(String username) {
        logger.debug("Getting user with username: {}", username);

        User user = userDao.getByUsername(username);
        if (user == null) {
            logger.error("Error getting user with username {}: Doesn't exists.", username);
            throw new UserDoesntExistsException();
        }

        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<User> getAllUsers() {
        logger.debug("Getting all users");

        return userDao.findAll();
    }

}
