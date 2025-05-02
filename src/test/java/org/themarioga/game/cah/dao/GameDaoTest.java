package org.themarioga.game.cah.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.dao.intf.DictionaryDao;
import org.themarioga.game.cah.dao.intf.GameDao;
import org.themarioga.game.cah.dao.intf.PlayerDao;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.commons.dao.intf.RoomDao;
import org.themarioga.game.commons.dao.intf.UserDao;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.models.Player;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/game.xml")
class GameDaoTest extends BaseTest {

    @Autowired
    private GameDao gameDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "CAHGame", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createGame() {
        Room room = roomDao.findOne(1L);
        User creator = userDao.findOne(1L);
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Game game = new Game();
        game.setStatus(GameStatusEnum.CREATED);
        game.setRoom(room);
        game.setCreator(creator);
        game.setDictionary(dictionary);
        game.setMaxNumberOfPlayers(1);
        game.setNumberOfPointsToWin(1);
        game.setNumberOfRounds(1);
        game.setPunctuationMode(PunctuationModeEnum.POINTS);
        game.setVotationMode(VotationModeEnum.DEMOCRACY);

        gameDao.create(game);

        Assertions.assertEquals(1L, game.getId());

        playerDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "CAHGame", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateGame() {
        Game game = gameDao.findOne(0L);
        game.setStatus(GameStatusEnum.STARTED);

        gameDao.update(game);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, game.getId());
    }

    @Test
    void deleteGame() {
        Game game = gameDao.findOne(0L);

        gameDao.delete(game);

        long total = gameDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findGame() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());
        Assertions.assertEquals(0, game.getRoom().getId());
        Assertions.assertEquals(0, game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void getByRoomId() {
        Game game = (Game) gameDao.getByRoom(roomDao.findOne(0L));

        Assertions.assertEquals(0L, game.getId());
        Assertions.assertEquals(0, game.getRoom().getId());
        Assertions.assertEquals(0, game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void findAllGames() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(0L, games.get(0).getId());
        Assertions.assertEquals(0, games.get(0).getRoom().getId());
        Assertions.assertEquals(0, games.get(0).getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, games.get(0).getStatus());
    }

    @Test
    void countAllGames() {
        long total = gameDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGameDeletionVotes-expected.xml", table = "game_deletion_votes", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addDeletionVoteToTable() {
        Game game = gameDao.findOne(0L);
        Player player = playerDao.findOne(0L);

        game.getDeletionVotes().add(player);

        gameDao.update(game);
        getCurrentSession().flush();

        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/gamedeletionvotes.xml")
    void getTableDeletionVotes() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getDeletionVotes());
        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    void findPlayersInGame() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(1, game.getPlayers().size());
    }

}

