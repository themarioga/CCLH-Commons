package org.themarioga.cclh.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.daos.intf.GameDao;
import org.themarioga.cclh.models.Game;
import org.themarioga.cclh.services.intf.GameService;

public class GameServiceImpl implements GameService {

    private Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    GameDao gameDao;

    @Override
    public Game create(Game game) {
        logger.debug("Creating game: {}", game);

        return gameDao.create(game);
    }

    @Override
    public Game update(Game game) {
        logger.debug("Updating game: {}", game);

        return gameDao.update(game);
    }

    @Override
    public void delete(Game game) {
        logger.debug("Delete game: {}", game);

        gameDao.delete(game);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete game by ID: {}", id);

        gameDao.deleteById(id);
    }

    @Override
    public Game findOne(long id) {
        logger.debug("Getting game with ID: {}", id);

        return gameDao.findOne(id);
    }

}
