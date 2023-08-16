package org.themarioga.cclh.commons.daos;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.daos.intf.*;
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
public class TableDaoTest extends BaseTest {

    @Autowired
    private GameDao gameDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private TableDao tableDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testCreateGame-expected.xml", table = "T_GAME")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreateTable-expected.xml", table = "T_TABLE")
    public void createGameAndTable() {
        Room room = roomDao.findOne(0);
        User creator = userDao.findOne(0);
        Dictionary dictionary = dictionaryDao.findOne(0);
        Card card = cardDao.findOne(0);

        Game game = new Game();
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CONFIGURED);
        game.setRoom(room);
        game.setCreator(creator);
        game.setDictionary(dictionary);

        gameDao.create(game);

        Table table = new Table();
        table.setGameId(game.getId());
        table.setCurrentRoundNumber(1);
        table.setCurrentPresident(creator);
        table.setCurrentBlackCard(card);
        game.setTable(table);

        gameDao.update(game);

        gameDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/game/testUpdateGame-expected.xml", table = "T_GAME")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testUpdateTable-expected.xml", table = "T_TABLE")
    public void updateGameAndTable() {
        User president = userDao.findOne(0);
        Card card = cardDao.findOne(0);

        Game game = gameDao.findOne(0L);
        game.setType(GameTypeEnum.DICTATORSHIP);
        game.setNumberOfPlayers(5);
        game.setNumberOfCardsToWin(5);
        game.setStatus(GameStatusEnum.CONFIGURED);
        game.getTable().setCurrentRoundNumber(1);
        game.getTable().setCurrentPresident(president);
        game.getTable().setCurrentBlackCard(card);

        gameDao.update(game);
        gameDao.getCurrentSession().flush();
    }

    @Test
    public void findTable() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertEquals(0, game.getTable().getCurrentRoundNumber());
        Assertions.assertEquals(0, game.getTable().getCurrentPresident().getId());
        Assertions.assertEquals(0, game.getTable().getCurrentBlackCard().getId());
    }

    @Test
    public void findAllTables() {
        List<Game> games = gameDao.findAll();

        Assertions.assertEquals(1, games.size());

        Assertions.assertEquals(0L, games.get(0).getId());

        Assertions.assertEquals(0, games.get(0).getTable().getCurrentRoundNumber());
        Assertions.assertEquals(0, games.get(0).getTable().getCurrentPresident().getId());
        Assertions.assertEquals(0, games.get(0).getTable().getCurrentBlackCard().getId());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/tabledeck.xml")
    public void getTableDeck() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getTable().getDeck());
        Assertions.assertEquals(1, game.getTable().getDeck().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreatePlayedCard-expected.xml", table = "t_table_playedcards")
    public void addTablePlayedCard() {
        Table table = tableDao.findOne(0L);
        Card card = cardDao.findOne(0L);
        Player played = playerDao.findOne(0L);

        PlayedCard playedCard = new PlayedCard();
        playedCard.setGameId(table.getGameId());
        playedCard.setCard(card);
        playedCard.setPlayer(played);
        table.getPlayedCards().add(playedCard);

        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayedcards.xml")
    public void getTablePlayedCards() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getTable().getPlayedCards());
        Assertions.assertEquals(1, game.getTable().getPlayedCards().size());
        Assertions.assertEquals("First", game.getTable().getPlayedCards().get(0).getCard().getText());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/table/testCreatePlayerVote-expected.xml", table = "t_table_playervotes")
    public void addTablePlayerVote() {
        Table table = tableDao.findOne(0L);
        Card card = cardDao.findOne(0L);
        Player played = playerDao.findOne(0L);

        PlayerVote playerVote = new PlayerVote();
        playerVote.setGameId(table.getGameId());
        playerVote.setCard(card);
        playerVote.setPlayer(played);
        table.getPlayerVotes().add(playerVote);

        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/player.xml")
    @DatabaseSetup("classpath:dbunit/dao/setup/tableplayervotes.xml")
    public void getTablePlayerVotes() {
        Game game = gameDao.findOne(0L);

        Assertions.assertEquals(0L, game.getId());

        Assertions.assertNotNull(game.getTable().getPlayerVotes());
        Assertions.assertEquals(1, game.getTable().getPlayerVotes().size());
        Assertions.assertEquals("First", game.getTable().getPlayerVotes().get(0).getCard().getText());
    }

}
