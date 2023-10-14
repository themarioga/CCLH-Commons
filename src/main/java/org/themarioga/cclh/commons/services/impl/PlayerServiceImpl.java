package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.PlayerService;
import org.themarioga.cclh.commons.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerDao playerDao;
    private final ConfigurationService configurationService;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao, ConfigurationService configurationService) {
        this.playerDao = playerDao;
        this.configurationService = configurationService;
    }

    @Override
    public Player create(Game game, User user) {
        logger.debug("Creating player from user {} in game {}", user, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Create player
        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        player.setPoints(0);
        player.setJoinOrder(game.getPlayers().size());
        player.setCreationDate(new Date());
        return playerDao.create(player);
    }

    @Override
    public Player findOne(long id) {
        logger.debug("Getting player with ID: {}", id);

        return playerDao.findOne(id);
    }

    @Override
    public void addCardsToPlayerDeck(Player player, List<Card> playerCards) {
        logger.debug("Adding cards to player {}", player);

        player.getDeck().addAll(playerCards);
        playerDao.update(player);
    }

    @Override
    public void transferCardsFromPlayerDeckToPlayerHand(Player player) {
        logger.debug("Transferring white cards from deck to hand from player {}", player);

        int cardsInHand = Integer.parseInt(configurationService.getConfiguration("game_whitecards_in_hand"));

        if (player.getHand().size() < cardsInHand) {
            int missingCards = cardsInHand - player.getHand().size();
            List<Card> cardsToTransfer = player.getDeck().subList(0, Math.min(missingCards, player.getDeck().size()));
            player.getHand().addAll(cardsToTransfer);
            player.getDeck().removeAll(cardsToTransfer);
            playerDao.update(player);
        }
    }

}
