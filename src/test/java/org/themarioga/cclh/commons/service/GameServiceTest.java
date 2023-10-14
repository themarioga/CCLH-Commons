package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.services.intf.GameService;
import org.themarioga.cclh.commons.services.intf.TableService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/table.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class GameServiceTest extends BaseTest {

    @Autowired
    GameService gameService;
//
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
