package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.*;
import org.themarioga.cclh.commons.enums.GamePunctuationTypeEnum;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/game.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createGame() {
        Room room = roomDao.findOne(1L);
        User creator = userDao.findOne(0L);
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Game game = new Game();
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setPunctuationType(GamePunctuationTypeEnum.ROUNDS);
        game.setMaxNumberOfPlayers(5);
        game.setNumberOfRounds(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CREATED);
        game.setRoom(room);
        game.setCreator(creator);
        game.setDictionary(dictionary);

        gameDao.create(game);

        Assertions.assertEquals(1L, game.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateGame() {
        Game game = gameDao.findOne(0L);
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setMaxNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);

        gameDao.create(game);
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
        Assertions.assertEquals(0, game.getNumberOfCardsToWin());
        Assertions.assertEquals(0, game.getMaxNumberOfPlayers());
        Assertions.assertEquals(0, game.getRoom().getId());
        Assertions.assertEquals(0, game.getCreator().getId());
        Assertions.assertEquals(0, game.getDictionary().getId());
        Assertions.assertEquals(GameTypeEnum.DEMOCRACY, game.getType());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void getByRoomId() {
        Game game = gameDao.getByRoom(roomDao.findOne(0L));

        Assertions.assertEquals(0L, game.getId());
        Assertions.assertEquals(0, game.getNumberOfCardsToWin());
        Assertions.assertEquals(0, game.getMaxNumberOfPlayers());
        Assertions.assertEquals(0, game.getRoom().getId());
        Assertions.assertEquals(0, game.getCreator().getId());
        Assertions.assertEquals(0, game.getDictionary().getId());
        Assertions.assertEquals(GameTypeEnum.DEMOCRACY, game.getType());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void findAllGames() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(0L, games.get(0).getId());
        Assertions.assertEquals(0, games.get(0).getNumberOfCardsToWin());
        Assertions.assertEquals(0, games.get(0).getMaxNumberOfPlayers());
        Assertions.assertEquals(0, games.get(0).getRoom().getId());
        Assertions.assertEquals(0, games.get(0).getCreator().getId());
        Assertions.assertEquals(0, games.get(0).getDictionary().getId());
        Assertions.assertEquals(GameTypeEnum.DEMOCRACY, games.get(0).getType());
        Assertions.assertEquals(GameStatusEnum.CREATED, games.get(0).getStatus());
    }

    @Test
    void countAllGames() {
        long total = gameDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGameDeletionVotes-expected.xml", table = "T_GAME_DELETIONVOTES", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addDeletionVoteToTable() {
        Game game = gameDao.findOne(0L);
        Player player = playerDao.findOne(0L);

        game.getDeletionVotes().add(player);

        gameDao.update(game);
        getCurrentSession().flush();

        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tabledeletionvotes.xml")
    void getTableDeletionVotes() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getDeletionVotes());
        Assertions.assertEquals(1, game.getDeletionVotes().size());
    }

    @Test
    void findPlayersInGame() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(1, game.getPlayers().size());
    }

}

