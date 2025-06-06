package org.themarioga.game.cah.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.game.cah.dao.intf.PlayerDao;
import org.themarioga.game.cah.exceptions.player.PlayerCannotPlayCardException;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.GameAlreadyStartedException;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.UserService;
import org.themarioga.game.commons.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerDao playerDao;
    private final UserService userService;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao, UserService userService) {
        this.playerDao = playerDao;
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Player create(Game game, User user) {
        logger.debug("Creating player from user {} in game {}", user, game);

        // Check game is not null
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game is already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Check user is not null
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Check if the user is already playing
        if (playerDao.findPlayerByUser(user) != null)
            throw new PlayerAlreadyExistsException();

        // Create player
        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        player.setJoinOrder(game.getPlayers().size());
        player.setCreationDate(new Date());
        return playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Player player) {
        logger.debug("Delete player {}", player);

        playerDao.delete(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void transferWhiteCardsFromGameDeckToPlayerHand(Player player, List<DeckCard> cardsToTransfer) {
        logger.debug("Transferring white cards from deck to hand from player {}", player);

        for (DeckCard gameDeckCard : cardsToTransfer) {
            PlayerHandCard playerHandCard = new PlayerHandCard();
            playerHandCard.setPlayer(player);
            playerHandCard.setCard(gameDeckCard.getCard());

            player.getHand().add(playerHandCard);
        }

        playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void incrementPoints(Player player) {
        logger.debug("Incrementing player's ({}) points", player);

        player.setPoints(player.getPoints() + 1);

        playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void removeCardFromHand(Player player, Card card) {
        logger.debug("Removing card {} from the hand of the player {}", card, player);

        Optional<PlayerHandCard> cards = player.getHand().stream().filter(playerHandCard -> playerHandCard.getCard().getId().equals(card.getId())).findFirst();

        if (cards.isEmpty())
            throw new PlayerCannotPlayCardException();

        player.getHand().remove(cards.get());
        playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Player findById(UUID id) {
        logger.debug("Getting player with ID: {}", id);

        return playerDao.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Player findByUser(User user) {
        logger.debug("Getting player with user: {}", user);

        return (Player) playerDao.findPlayerByUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Player findByUserId(UUID userId) {
        logger.debug("Getting player with user ID: {}", userId);

        return (Player) playerDao.findPlayerByUser(userService.getById(userId));
    }

}
