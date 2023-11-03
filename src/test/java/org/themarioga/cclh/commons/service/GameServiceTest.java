package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.services.intf.DictionaryService;
import org.themarioga.cclh.commons.services.intf.GameService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/table.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class GameServiceTest extends BaseTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    GameService gameService;

    @Autowired
    DictionaryService dictionaryService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "T_GAME")
    void testCreateGame_CreateRoom() {
        Game game = gameService.create(2L, "Room 3", 0L, 0L);

        gameDao.getCurrentSession().flush();

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertNotNull(game.getRoom().getId());
        Assertions.assertNotNull(game.getCreator().getId());

        Assertions.assertEquals(2L, game.getRoom().getId());
        Assertions.assertEquals(0L, game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteGame-expected.xml", table = "T_GAME")
    void testDeleteGame() {
        gameService.delete(0L);

        gameDao.getCurrentSession().flush();

        Game game = gameService.getByRoomId(0L);

        Assertions.assertNull(game);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameType-expected.xml", table = "T_GAME")
    void testSetType() {
        Game game = gameService.getByRoomId(0L);

        game = gameService.setType(game, GameTypeEnum.DICTATORSHIP);

        gameDao.getCurrentSession().flush();

        Assertions.assertEquals(GameTypeEnum.DICTATORSHIP, game.getType());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberCards-expected.xml", table = "T_GAME")
    void testSetNumberOfCardsToWin() {
        Game game = gameService.getByRoomId(0L);

        game = gameService.setNumberOfCardsToWin(game, 5);

        gameDao.getCurrentSession().flush();

        Assertions.assertEquals(5, game.getNumberOfCardsToWin());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "T_GAME")
    void testSetMaxNumberOfPlayers() {
        Game game = gameService.getByRoomId(0L);

        game = gameService.setMaxNumberOfPlayers(game, 5);

        gameDao.getCurrentSession().flush();

        Assertions.assertEquals(5, game.getMaxNumberOfPlayers());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameDictionary-expected.xml", table = "T_GAME")
    void testSetDictionary() {
        Game game = gameService.getByRoomId(0L);

        game = gameService.setDictionary(game, 1L);

        gameDao.getCurrentSession().flush();

        Assertions.assertEquals(dictionaryService.findOne(1L), game.getDictionary());
    }

}
