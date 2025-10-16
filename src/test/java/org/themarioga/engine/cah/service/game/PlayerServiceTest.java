package org.themarioga.engine.cah.service.game;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.exceptions.player.PlayerCannotPlayCardException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.services.intf.dictionaries.CardService;
import org.themarioga.engine.cah.services.intf.game.GameService;
import org.themarioga.engine.cah.services.intf.game.PlayerService;
import org.themarioga.engine.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.engine.commons.models.User;
import org.themarioga.engine.commons.services.intf.RoomService;
import org.themarioga.engine.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/player.xml")
class PlayerServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;
    @Autowired
    CardService cardService;

    @Test
    void testCreate() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));

        Player player = playerService.create(game, user);

        Assertions.assertNotNull(player);
    }

    @Test
    void testCreate_Duplicated() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(PlayerAlreadyExistsException.class, () -> playerService.create(game, user));
    }

    @Test
    void testDelete() {
        playerService.delete(playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        getCurrentSession().flush();

        Assertions.assertNull(playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/player2.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/game/deckcard.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/game/round.xml")
    void testInsertWhiteCardsIntoPlayerHand() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findByUserId(UUID.fromString("77777777-7777-7777-7777-777777777777"));
        playerService.insertWhiteCardsIntoPlayerHand(player, game.getWhiteCardsDeck().subList(0, 5));

        Assertions.assertNotNull(player);
        Assertions.assertEquals(5, player.getHand().size());
    }

    @Test
    void testIncrementPoints() {
        Player player = playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        player = playerService.incrementPoints(player);

        Assertions.assertNotNull(player);
        Assertions.assertEquals(1, player.getPoints());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/game/playerhand.xml")
    void testRemoveCardFromHand() {
        Player player = playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        player = playerService.removeCardFromHand(player, card);

        Assertions.assertNotNull(player);
        Assertions.assertEquals(0, player.getHand().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
    void testRemoveCardFromHand_NonExistentCard() {
        Player player = playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        Assertions.assertThrows(PlayerCannotPlayCardException.class, () -> playerService.removeCardFromHand(player, card));
    }

    @Test
    void testFindPlayerById() {
        Player player = playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getGame().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getUser().getId());
    }

    @Test
    void testFindPlayerByUser() {
        Player player = playerService.findByUser(userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getGame().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getUser().getId());
    }

    @Test
    void testFindPlayerByUserId() {
        Player player = playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getGame().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getUser().getId());
    }

}
