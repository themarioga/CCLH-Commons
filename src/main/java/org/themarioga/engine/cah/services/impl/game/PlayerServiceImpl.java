package org.themarioga.engine.cah.services.impl.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.engine.cah.dao.intf.game.PlayerDao;
import org.themarioga.engine.cah.exceptions.player.PlayerCannotPlayCardException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.game.PlayerHandCard;
import org.themarioga.engine.cah.services.intf.game.PlayerService;
import org.themarioga.engine.commons.enums.ErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;
import org.themarioga.engine.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.engine.commons.models.User;
import org.themarioga.engine.commons.services.intf.UserService;
import org.themarioga.engine.commons.util.Assert;

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
    public void insertWhiteCardsIntoPlayerHand(Player player, List<Card> cardsToTransfer) {
        logger.debug("Transferring white cards to hand of player {}", player);

        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        for (Card card : cardsToTransfer) {
            PlayerHandCard playerHandCard = new PlayerHandCard();
            playerHandCard.setPlayer(player);
            playerHandCard.setCard(card);

            player.getHand().add(playerHandCard);
        }

        playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Player incrementPoints(Player player) {
        logger.debug("Incrementing player's ({}) points", player);

        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        player.setPoints(player.getPoints() + 1);

        return playerDao.createOrUpdate(player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Player removeCardFromHand(Player player, Card card) {
        logger.debug("Removing card {} from the hand of the player {}", card, player);

        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        Optional<PlayerHandCard> cards = player.getHand().stream().filter(playerHandCard -> playerHandCard.getCard().getId().equals(card.getId())).findFirst();

        if (cards.isEmpty())
            throw new PlayerCannotPlayCardException();

        player.getHand().remove(cards.get());

        return playerDao.createOrUpdate(player);
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

        return playerDao.findPlayerByUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Player findByUserId(UUID userId) {
        logger.debug("Getting player with user ID: {}", userId);

        return playerDao.findPlayerByUser(userService.getById(userId));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Player findPlayerByGameAndUser(Game game, User user) {
        logger.debug("Checking user's playing game {}", user);

        return playerDao.findPlayerByUserAndGame(user, game);
    }

}
