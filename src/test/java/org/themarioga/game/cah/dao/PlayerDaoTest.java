package org.themarioga.game.cah.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.dao.intf.GameDao;
import org.themarioga.game.cah.dao.intf.PlayerDao;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.commons.dao.intf.UserDao;
import org.themarioga.game.commons.models.Game;
import org.themarioga.game.commons.models.User;

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
    private PlayerDao playerDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testCreatePlayer-expected.xml", table = "CAHPlayer", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createPlayer() {
        Game game = gameDao.findOne(0L);
        User user = userDao.findOne(1L);

        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        player.setJoinOrder(1);

        playerDao.create(player);

        Assertions.assertEquals(1L, player.getId());

        playerDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/player/testUpdatePlayer-expected.xml", table = "CAHPlayer", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updatePlayer() {
        Player player = playerDao.findOne(0L);
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
        org.themarioga.game.commons.models.Player player = playerDao.findOne(0L);

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

}
