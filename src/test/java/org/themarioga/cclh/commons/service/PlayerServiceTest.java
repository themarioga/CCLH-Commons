package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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
    DeckService deckService;
    @Autowired
    CardService cardService;

    @Test
    void testCreate() {
        Game game = gameService.getByRoom(roomService.getById(0L));
        User user = userService.getById(4L);

        Player player = playerService.create(game, user);

        Assertions.assertNotNull(player);
    }

    @Test
    void testFindPlayerByUser() {
        Player player = playerService.findByUser(userService.getById(0L));

        Assertions.assertEquals(10L, player.getGame().getId());
        Assertions.assertEquals(0L, player.getUser().getId());
    }

    @Test
    void testFindPlayerByUserId() {
        Player player = playerService.findByUserId(0L);

        Assertions.assertEquals(10L, player.getGame().getId());
        Assertions.assertEquals(0L, player.getUser().getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdatePlayersDeck-expected.xml", table = "T_PLAYER_DECK", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testAddCardsToPlayerDeck() {
        Player player = playerService.findByUserId(0L);

        List<Card> cards = cardService.findCardsByDeckIdAndType(deckService.findOne(0L), CardTypeEnum.WHITE);
        List<Card> card = cards.subList(0, 1);

        playerService.addCardsToPlayerDeck(player, card);

        getCurrentSession().flush();

        Assertions.assertEquals(3L, card.get(0).getId());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/playerdeck.xml")
    void testTransferCardsFromPlayerDeckToPlayerHand() {
        Player player = playerService.findByUserId(0L);

        Assertions.assertEquals(3, player.getDeck().size());
        Assertions.assertEquals("First white card", player.getDeck().get(0).getText());
        Assertions.assertEquals(0, player.getHand().size());

        playerService.transferCardsFromPlayerDeckToPlayerHand(player);

        Assertions.assertEquals(0, player.getDeck().size());
        Assertions.assertEquals(3, player.getHand().size());
        Assertions.assertEquals("First white card", player.getHand().get(0).getText());
    }

}
