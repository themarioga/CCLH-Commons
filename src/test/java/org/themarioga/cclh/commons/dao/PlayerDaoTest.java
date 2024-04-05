package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/game.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
class PlayerDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private PlayerDao playerDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testCreatePlayer-expected.xml", table = "T_PLAYER", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createPlayer() {
        Game game = gameDao.findOne(0L);
        User user = userDao.findOne(1L);

        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        player.setPoints(1);
        player.setJoinOrder(1);

        playerDao.create(player);

        Assertions.assertEquals(1L, player.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayer-expected.xml", table = "T_PLAYER", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updatePlayer() {
        Player player = playerDao.findOne(0L);
        player.setPoints(1);
        player.setJoinOrder(1);

        playerDao.update(player);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, player.getId());
    }

    @Test
    void deletePlayer() {
        Player player = playerDao.findOne(0L);

        playerDao.delete(player);

        long total = playerDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findPlayer() {
        Player player = playerDao.findOne(0L);

        Assertions.assertEquals(0L, player.getId());
    }

    @Test
    void findAllPlayers() {
        List<Player> players = playerDao.findAll();

        Assertions.assertEquals(1, players.size());
        Assertions.assertEquals(0L, players.get(0).getId());
    }

    @Test
    void countAllPlayers() {
        long total = playerDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayersHand-expected.xml", table = "T_PLAYER_HAND", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addCardsToPlayersHand() {
        Player player = playerDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        PlayerHandCard playerHandCard = new PlayerHandCard();
        playerHandCard.setPlayer(player);
        playerHandCard.setCard(card);

        player.getHand().add(playerHandCard);

        playerDao.update(player);
        getCurrentSession().flush();

        Assertions.assertEquals(1, player.getHand().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/playerhand.xml")
    void getPlayerHand() {
        Player player = playerDao.findOne(0L);

        Assertions.assertEquals(0L, player.getId());

        Assertions.assertNotNull(player.getHand());
        Assertions.assertEquals(1, player.getHand().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayedcards.xml")
    void getTablePlayedCardsByPlayerId() {
        PlayedCard card = playerDao.findCardByPlayer(0L);

        Assertions.assertEquals(0L, card.getTable().getGame().getId());
        Assertions.assertEquals(0L, card.getPlayer().getId());
        Assertions.assertEquals("First", card.getCard().getText());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayervotes.xml")
    void getTablePlayerVotesByPlayerId() {
        VotedCard vote = playerDao.findVotesByPlayer(0L);

        Assertions.assertEquals(0L, vote.getTable().getGame().getId());
        Assertions.assertEquals(0L, vote.getPlayer().getId());
        Assertions.assertEquals("First", vote.getCard().getText());
    }

}
