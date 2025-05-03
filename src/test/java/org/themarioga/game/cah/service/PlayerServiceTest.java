package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.game.GameAlreadyStartedException;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.game.commons.models.Game;
import org.themarioga.game.commons.models.Player;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.RoomService;
import org.themarioga.game.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class PlayerServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;

    @Test
    void testCreate() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Player player = playerService.create(game, user);

        Assertions.assertNotNull(player);
    }

    @Test
    void testCreate_AlreadyStarted() {
        org.themarioga.game.cah.models.Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        gameService.setStatus(game, GameStatusEnum.STARTED);

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> playerService.create(game, user));
    }

    @Test
    void testCreate_Duplicated() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        playerService.create(game, user);

        Assertions.assertThrows(PlayerAlreadyExistsException.class, () -> playerService.create(game, user));
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

    @Test
    void testDelete() {
        playerService.delete(playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    @Test
    void testFindPlayerById() {
        Player player = playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getGame().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), player.getUser().getId());
    }

}
