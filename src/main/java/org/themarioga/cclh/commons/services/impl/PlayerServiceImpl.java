package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.player.PlayerCannotPlayCardException;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.PlayerService;
import org.themarioga.cclh.commons.services.intf.UserService;
import org.themarioga.cclh.commons.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerDao playerDao;
    private final UserService userService;
    private final ConfigurationService configurationService;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao, UserService userService, ConfigurationService configurationService) {
        this.playerDao = playerDao;
        this.userService = userService;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Player create(Game game, long userId) {
        logger.debug("Creating player from user {} in game {}", userId, game);

        // Check game is not null
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check user is not null
        Assert.assertNotNull(userId, ErrorEnum.USER_NOT_FOUND);

        // Get the user
        User user = userService.getById(userId);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Check if the user is already playing
        if (playerDao.findPlayerByUser(user) != null)
            throw new PlayerAlreadyExistsException();

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
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void addCardsToPlayerDeck(Player player, List<Card> playerCards) {
        logger.debug("Adding cards to player {}", player);

        for (Card card : playerCards) {
            PlayerDeckCard playerDeckCard = new PlayerDeckCard();
            playerDeckCard.setPlayer(player);
            playerDeckCard.setCard(card);

            player.getDeck().add(playerDeckCard);
        }

        playerDao.update(player);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void transferCardsFromPlayerDeckToPlayerHand(Player player) {
        logger.debug("Transferring white cards from deck to hand from player {}", player);

        int cardsInHand = Integer.parseInt(configurationService.getConfiguration("game_whitecards_in_hand"));

        if (player.getHand().size() < cardsInHand) {
            int missingCards = cardsInHand - player.getHand().size();
            List<PlayerDeckCard> cardsToTransfer = new ArrayList<>(player.getDeck().subList(0, Math.min(missingCards, player.getDeck().size())));
            for (PlayerDeckCard playerDeckCard : cardsToTransfer) {
                PlayerHandCard playerHandCard = new PlayerHandCard();
                playerHandCard.setPlayer(player);
                playerHandCard.setCard(playerDeckCard.getCard());

                player.getHand().add(playerHandCard);
                player.getDeck().remove(playerDeckCard);
            }

            playerDao.update(player);
        }
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void removeCardFromHand(Player player, Card card) {
        logger.debug("Removing card {} from the hand of the player {}", card, player);

        Optional<PlayerHandCard> cards = player.getHand().stream().filter(playerHandCard -> playerHandCard.getCard().equals(card)).findFirst();

        if (cards.isEmpty())
            throw new PlayerCannotPlayCardException();

        player.getHand().remove(cards.get());
        playerDao.update(player);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Player findById(long id) {
        logger.debug("Getting player with ID: {}", id);

        return playerDao.findOne(id);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Player findByUser(User user) {
        logger.debug("Getting player with user: {}", user);

        return playerDao.findPlayerByUser(user);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Player findByUserId(long id) {
        logger.debug("Getting player with user ID: {}", id);

        return playerDao.findPlayerByUser(userService.getById(id));
    }

}
