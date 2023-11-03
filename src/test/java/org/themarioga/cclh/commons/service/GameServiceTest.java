package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.models.Game;
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

    @Test
    void testCreateGame() {
        Game game = gameService.create(1L, "Room 1", 0L, 0L);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertNotNull(game.getRoom().getId());
        Assertions.assertNotNull(game.getCreator().getId());

        Assertions.assertEquals(1L, game.getRoom().getId());
        Assertions.assertEquals(0L, game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void testDeleteGame() {
        gameService.delete(0L);

        gameDao.getCurrentSession().flush();

        Game game = gameService.getByRoomId(0L);

        Assertions.assertNull(game);
    }

    @Test
    void testSetType() {
        Game game = gameService.getByRoomId(0L);
    }

    @Test
    void testSetNumberOfCardsToWin() {

    }

    @Test
    void testSetMaxNumberOfPlayers() {

    }

    @Test
    void testSetDictionary() {

    }

//    @Test
//    void testAddBlackCardsToTableDeck() {
//        Game game = gameService.getByRoomId(0);
//
//        Assertions.assertEquals(0, game.getDeck().size());
//
//        gameService.addBlackCardsToTableDeck(game);
//
//        Assertions.assertEquals(3, game.getDeck().size());
//        Assertions.assertEquals(CardTypeEnum.BLACK, game.getDeck().get(0).getType());
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/tabledeck.xml")
//    void testTransferCardFromDeckToTable() {
//        Game game = gameService.findOne(0);
//
//        Assertions.assertEquals(3, game.getDeck().size());
//        Assertions.assertEquals(CardTypeEnum.BLACK, game.getDeck().get(0).getType());
//        Assertions.assertNull(game.getCurrentBlackCard());
//
//        gameService.transferCardFromDeckToTable(game);
//
//        Assertions.assertEquals(2, game.getDeck().size());
//        Assertions.assertEquals(CardTypeEnum.BLACK, game.getDeck().get(0).getType());
//        Assertions.assertNotNull(game.getCurrentBlackCard());
//        Assertions.assertEquals(CardTypeEnum.BLACK, game.getCurrentBlackCard().getType());
//    }
//
//    @Test
//    void testAddWhiteCardsToPlayersDecks() {
//        Game game = gameService.getByRoomId(0);
//
//        Assertions.assertEquals(game.getmaxnumberofplayers(), game.getPlayers().size());
//        Assertions.assertEquals(0, game.getPlayers().get(0).getDeck().size());
//
//        gameService.addWhiteCardsToPlayersDecks(game);
//
//        long cardsPerPlayer = Math.floorDiv(dictionaryService.countCardsByDictionaryIdAndType(game.getDictionary(), CardTypeEnum.WHITE), game.getmaxnumberofplayers());
//
//        Assertions.assertEquals(game.getmaxnumberofplayers(), game.getPlayers().size());
//        Assertions.assertEquals(cardsPerPlayer, game.getPlayers().get(0).getDeck().size());
//        Assertions.assertEquals(CardTypeEnum.WHITE, game.getPlayers().get(0).getDeck().get(0).getType());
//    }

}
