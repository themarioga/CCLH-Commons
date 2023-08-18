package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Table;
import org.themarioga.cclh.commons.services.intf.DictionaryService;
import org.themarioga.cclh.commons.services.intf.GameService;
import org.themarioga.cclh.commons.services.intf.TableService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/table.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class TableServiceTest extends BaseTest {

    @Autowired
    TableService tableService;

    @Autowired
    GameService gameService;

    @Autowired
    DictionaryService dictionaryService;

    @Test
    void testAddBlackCardsToTableDeck() {
        Game game = gameService.findOne(0);

        Assertions.assertEquals(0, game.getTable().getDeck().size());

        tableService.addBlackCardsToTableDeck(game);

        Assertions.assertEquals(3, game.getTable().getDeck().size());
        Assertions.assertEquals(CardTypeEnum.BLACK, game.getTable().getDeck().get(0).getType());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/tabledeck.xml")
    void testTransferCardFromDeckToTable() {
        Table table = tableService.findOne(0);

        Assertions.assertEquals(3, table.getDeck().size());
        Assertions.assertEquals(CardTypeEnum.BLACK, table.getDeck().get(0).getType());
        Assertions.assertNull(table.getCurrentBlackCard());

        tableService.transferCardFromDeckToTable(table);

        Assertions.assertEquals(2, table.getDeck().size());
        Assertions.assertEquals(CardTypeEnum.BLACK, table.getDeck().get(0).getType());
        Assertions.assertNotNull(table.getCurrentBlackCard());
        Assertions.assertEquals(CardTypeEnum.BLACK, table.getCurrentBlackCard().getType());
    }

    @Test
    void testAddWhiteCardsToPlayersDecks() {
        Game game = gameService.findOne(0);

        Assertions.assertEquals(game.getNumberOfPlayers(), game.getPlayers().size());
        Assertions.assertEquals(0, game.getPlayers().get(0).getDeck().size());

        tableService.addWhiteCardsToPlayersDecks(game);

        long cardsPerPlayer = Math.floorDiv(dictionaryService.countCardsByDictionaryIdAndType(game.getDictionary(), CardTypeEnum.WHITE), game.getNumberOfPlayers());

        Assertions.assertEquals(game.getNumberOfPlayers(), game.getPlayers().size());
        Assertions.assertEquals(cardsPerPlayer, game.getPlayers().get(0).getDeck().size());
        Assertions.assertEquals(CardTypeEnum.WHITE, game.getPlayers().get(0).getDeck().get(0).getType());
    }

}
