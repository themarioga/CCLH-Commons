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

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createGame() {
        Room room = roomDao.findOne(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        User creator = userDao.findOne(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

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
        game.setCreationDate(new Date());

        game = gameDao.createOrUpdate(game);
        getCurrentSession().flush();

        Assertions.assertNotNull(game.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateGame() {
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        game.setStatus(GameStatusEnum.STARTED);

        gameDao.createOrUpdate(game);
        getCurrentSession().flush();

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getId());
    }

    @Test
    void deleteGame() {
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        gameDao.delete(game);

        long total = gameDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findGame() {
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void getByRoomId() {
        Game game = (Game) gameDao.getByRoom(roomDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void findAllGames() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), games.get(0).getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), games.get(0).getRoom().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), games.get(0).getCreator().getId());
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
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player = playerDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        game.getDeletionVotes().add(player);

        gameDao.createOrUpdate(game);
        getCurrentSession().flush();

        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/gamedeletionvotes.xml")
    void getTableDeletionVotes() {
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getId());

        Assertions.assertNotNull(game.getDeletionVotes());
        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    void findPlayersInGame() {
        Game game = gameDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(1, game.getPlayers().size());
    }

}

