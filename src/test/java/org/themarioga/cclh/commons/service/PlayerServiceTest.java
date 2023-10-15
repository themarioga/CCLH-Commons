package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.*;

import java.util.List;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
class PlayerServiceTest extends BaseTest {

    @Autowired
    PlayerDao playerDao;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;
    @Autowired
    DictionaryService dictionaryService;

    @Test
    void testCreate() {
        Game game = gameService.getByRoomId(roomService.getById(0L));
        User user = userService.getById(0L);

        Player player = playerService.create(game, user);

        Assertions.assertNotNull(player);
    }

    @Test
    void testFindPlayer() {
        Player player = playerService.findOne(10L);

        Assertions.assertEquals(10L, player.getGame().getId());
        Assertions.assertEquals(0L, player.getUser().getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdatePlayersDeck-expected.xml", table = "T_PLAYER_DECK")
    void testAddCardsToPlayerDeck() {
        Player player = playerService.findOne(10L);

        List<Card> cards = dictionaryService.findCardsByDictionaryIdAndType(dictionaryService.findOne(0L), CardTypeEnum.WHITE);
        List<Card> card = cards.subList(0, 1);

        playerService.addCardsToPlayerDeck(player, card);
        playerDao.getCurrentSession().flush();

        Assertions.assertEquals(3L, card.get(0).getId());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/playerdeck.xml")
    void testTransferCardsFromPlayerDeckToPlayerHand() {
        Player player = playerService.findOne(0L);

        Assertions.assertEquals(3, player.getDeck().size());
        Assertions.assertEquals("First white card", player.getDeck().get(0).getText());
        Assertions.assertEquals(0, player.getHand().size());

        playerService.transferCardsFromPlayerDeckToPlayerHand(player);

        Assertions.assertEquals(0, player.getDeck().size());
        Assertions.assertEquals(3, player.getHand().size());
        Assertions.assertEquals("First white card", player.getHand().get(0).getText());
    }

}
