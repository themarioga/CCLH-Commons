package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.*;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/room.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/game.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/table.xml")
class GameDaoTest extends BaseTest {

    @Autowired
    private GameDao gameDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "T_GAME")
    void createGame() {
        Room room = roomDao.findOne(0);
        User creator = userDao.findOne(0);
        Dictionary dictionary = dictionaryDao.findOne(0);

        Game game = new Game();
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CONFIGURED);
        game.setRoom(room);
        game.setCreator(creator);
        game.setDictionary(dictionary);

        gameDao.create(game);
        gameDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "T_GAME")
    void updateGame() {
        Game game = gameDao.findOne(0L);
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);

        gameDao.update(game);
        gameDao.getCurrentSession().flush();
    }

    @Test
    void deleteGame() {
        Game game = gameDao.findOne(0L);

        gameDao.delete(game);
        gameDao.getCurrentSession().flush();

        long total = gameDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findGame() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());
        Assertions.assertEquals(0, game.getNumberOfCardsToWin());
        Assertions.assertEquals(0, game.getNumberOfPlayers());
        Assertions.assertEquals(0, game.getRoom().getId());
        Assertions.assertEquals(0, game.getCreator().getId());
        Assertions.assertEquals(0, game.getDictionary().getId());
        Assertions.assertEquals(GameTypeEnum.DEMOCRACY, game.getType());
        Assertions.assertEquals(GameStatusEnum.CONFIGURED, game.getStatus());
    }

    @Test
    void findAllGames() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(0L, games.get(0).getId());
        Assertions.assertEquals(0, games.get(0).getNumberOfCardsToWin());
        Assertions.assertEquals(0, games.get(0).getNumberOfPlayers());
        Assertions.assertEquals(0, games.get(0).getRoom().getId());
        Assertions.assertEquals(0, games.get(0).getCreator().getId());
        Assertions.assertEquals(0, games.get(0).getDictionary().getId());
        Assertions.assertEquals(GameTypeEnum.DEMOCRACY, games.get(0).getType());
        Assertions.assertEquals(GameStatusEnum.CONFIGURED, games.get(0).getStatus());
    }

    @Test
    void countAllGames() {
        long total = gameDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGameDeck-expected.xml", table = "T_TABLE_DECK")
    void addCardsToGameHand() {
        Game game = gameDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        game.getDeck().add(card);

        gameDao.update(game);
        gameDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGameDeletionVotes-expected.xml", table = "T_GAME_DELETIONVOTES")
    void addDeletionVoteToTable() {
        Game game = gameDao.findOne(0L);
        Player player = playerDao.findOne(0L);

        game.getDeletionVotes().add(player);

        gameDao.update(game);
        gameDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tabledeletionvotes.xml")
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

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tabledeck.xml")
    void getDeck() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getDeck());
        Assertions.assertEquals(1, game.getDeck().size());
    }

}

