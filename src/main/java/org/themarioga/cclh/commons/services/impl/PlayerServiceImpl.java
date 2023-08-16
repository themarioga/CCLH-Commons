package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.daos.intf.PlayerDao;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.services.intf.PlayerService;

public class PlayerServiceImpl implements PlayerService {

    private Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    PlayerDao playerDao;

    @Override
    public Player create(Player player) {
        logger.debug("Creating player: {}", player);

        return playerDao.create(player);
    }

    @Override
    public Player update(Player player) {
        logger.debug("Updating player: {}", player);

        return playerDao.update(player);
    }

    @Override
    public void delete(Player player) {
        logger.debug("Delete player: {}", player);

        playerDao.delete(player);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete player by ID: {}", id);

        playerDao.deleteById(id);
    }

    @Override
    public Player findOne(long id) {
        logger.debug("Getting player with ID: {}", id);

        return playerDao.findOne(id);
    }

}
