package org.themarioga.cclh.commons.daos;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.daos.intf.CardDao;
import org.themarioga.cclh.commons.daos.intf.GameDao;
import org.themarioga.cclh.commons.daos.intf.PlayerDao;
import org.themarioga.cclh.commons.daos.intf.UserDao;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.models.*;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/game.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
public class PlayerDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private PlayerDao playerDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testCreatePlayer-expected.xml", table = "T_PLAYER")
    public void createPlayer() {
        User user = userDao.findOne(0);
        Game game = gameDao.findOne(0);

        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setPoints(1);
        player.setJoinOrder(1);

        playerDao.create(player);
        playerDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayer-expected.xml", table = "T_PLAYER")
    public void updatePlayer() {
        Player player = playerDao.findOne(0L);
        player.setPoints(1);
        player.setJoinOrder(1);

        playerDao.update(player);
        playerDao.getCurrentSession().flush();
    }

    @Test
    public void deletePlayer() {
        Player player = playerDao.findOne(0L);

        playerDao.delete(player);
        playerDao.getCurrentSession().flush();

        long total = playerDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    public void findPlayer() {
        Player player = playerDao.findOne(0L);

        Assertions.assertEquals(0L, player.getId());
    }

    @Test
    public void findAllPlayers() {
        List<Player> players = playerDao.findAll();

        Assertions.assertEquals(1, players.size());
        Assertions.assertEquals(0L, players.get(0).getId());
    }

    @Test
    public void countAllPlayers() {
        long total = playerDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayersDeck-expected.xml", table = "T_PLAYER_DECK")
    public void addCardsToPlayersDeck() {
        Player player = playerDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        player.getDeck().add(card);

        playerDao.update(player);
        playerDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayersHand-expected.xml", table = "T_PLAYER_HAND")
    public void addCardsToPlayersHand() {
        Player player = playerDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        player.getHand().add(card);

        playerDao.update(player);
        playerDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/playerdeck.xml")
    public void getPlayerDeck() {
        Player player = playerDao.findOne(0L);

        Assertions.assertEquals(0L, player.getId());

        Assertions.assertNotNull(player.getDeck());
        Assertions.assertEquals(1, player.getDeck().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/playerhand.xml")
    public void getPlayerHand() {
        Player player = playerDao.findOne(0L);

        Assertions.assertEquals(0L, player.getId());

        Assertions.assertNotNull(player.getHand());
        Assertions.assertEquals(1, player.getHand().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayedcards.xml")
    public void getTablePlayedCardsByPlayerId() {
        PlayedCard card = playerDao.findCardByPlayer(0L);

        Assertions.assertEquals(0L, card.getGameId());
        Assertions.assertEquals(0L, card.getPlayer().getId());
        Assertions.assertEquals("First", card.getCard().getText());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayervotes.xml")
    public void getTablePlayerVotesByPlayerId() {
        PlayerVote vote = playerDao.findVotesByPlayer(0L);

        Assertions.assertEquals(0L, vote.getGameId());
        Assertions.assertEquals(0L, vote.getPlayer().getId());
        Assertions.assertEquals("First", vote.getCard().getText());
    }

}
