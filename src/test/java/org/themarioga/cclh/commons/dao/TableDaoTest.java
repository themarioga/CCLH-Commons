package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.*;
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
class TableDaoTest extends BaseTest {

    @Autowired
    private TableDao tableDao;
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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreateTable-expected.xml", table = "T_TABLE", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createGameAndTable() {
        Room room = roomDao.findOne(1L);
        User creator = userDao.findOne(0L);
        Dictionary dictionary = dictionaryDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        Game game = new Game();
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setMaxNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CREATED);
        game.setRoom(room);
        game.setCreator(creator);
        game.setDictionary(dictionary);

        gameDao.create(game);

        Table table = new Table();
        table.setGameId(game.getId());
        table.setCurrentRoundNumber(1);
        table.setCurrentPresident(playerDao.findPlayerByUser(creator));
        table.setCurrentBlackCard(card);
        game.setTable(table);

        gameDao.create(game);
        getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testUpdateTable-expected.xml", table = "T_TABLE", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateGameAndTable() {
        User president = userDao.findOne(0L);
        Card card = cardDao.findOne(0L);

        Game game = gameDao.findOne(0L);
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setMaxNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CREATED);
        game.getTable().setCurrentRoundNumber(1);
        game.getTable().setCurrentPresident(playerDao.findPlayerByUser(president));
        game.getTable().setCurrentBlackCard(card);

        gameDao.update(game);
        getCurrentSession().flush();
    }

    @Test
    void findTable() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertEquals(0, game.getTable().getCurrentRoundNumber());
        Assertions.assertEquals(0, game.getTable().getCurrentPresident().getId());
        Assertions.assertEquals(0, game.getTable().getCurrentBlackCard().getId());
    }

    @Test
    void findAllTables() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(0L, games.get(0).getId());

        Assertions.assertEquals(0, games.get(0).getTable().getCurrentRoundNumber());
        Assertions.assertEquals(0, games.get(0).getTable().getCurrentPresident().getId());
        Assertions.assertEquals(0, games.get(0).getTable().getCurrentBlackCard().getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreatePlayedCard-expected.xml", table = "t_table_playedcards", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addTablePlayedCard() {
        Table table = tableDao.findOne(0L);
        Card card = cardDao.findOne(0L);
        Player played = playerDao.findOne(0L);

        PlayedCard playedCard = new PlayedCard();
        playedCard.setGameId(table.getGameId());
        playedCard.setCard(card);
        playedCard.setPlayer(played);
        table.getPlayedCards().add(playedCard);

        tableDao.update(table);
        getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayedcards.xml")
    void getTablePlayedCards() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getTable().getPlayedCards());
        Assertions.assertEquals(1, game.getTable().getPlayedCards().size());
        Assertions.assertEquals("First", game.getTable().getPlayedCards().get(0).getCard().getText());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreatePlayerVote-expected.xml", table = "t_table_playervotes", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addTablePlayerVote() {
        Table table = tableDao.findOne(0L);
        Card card = cardDao.findOne(0L);
        Player played = playerDao.findOne(0L);

        PlayerVote playerVote = new PlayerVote();
        playerVote.setGameId(table.getGameId());
        playerVote.setCard(card);
        playerVote.setPlayer(played);
        table.getPlayerVotes().add(playerVote);

        tableDao.update(table);
        getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayervotes.xml")
    void getTablePlayerVotes() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getTable().getPlayerVotes());
        Assertions.assertEquals(1, game.getTable().getPlayerVotes().size());
        Assertions.assertEquals("First", game.getTable().getPlayerVotes().get(0).getCard().getText());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayedcards.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayervotes.xml")
    void testGetMostVotedCard() {
        PlayedCard card = tableDao.getMostVotedCard(0L);

        Assertions.assertNotNull(card);
        Assertions.assertEquals(0, card.getCard().getId());
    }

}
