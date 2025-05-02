package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.game.GameAlreadyStartedException;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.game.commons.models.Game;
import org.themarioga.game.commons.models.Player;
import org.themarioga.game.commons.services.intf.GameService;
import org.themarioga.game.commons.services.intf.RoomService;
import org.themarioga.game.commons.services.intf.UserService;

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
        Game game = gameService.getByRoom(roomService.getById(0L));

        Player player = playerService.create(game, 4L);

        Assertions.assertNotNull(player);
    }

    @Test
    void testCreate_AlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(0L));

        gameService.setStatus(game, GameStatusEnum.STARTED);

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> playerService.create(game, 4L));
    }

    @Test
    void testCreate_Duplicated() {
        Game game = gameService.getByRoom(roomService.getById(0L));

        playerService.create(game, 4L);

        Assertions.assertThrows(PlayerAlreadyExistsException.class, () -> playerService.create(game, 4L));
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
    void testDelete() {
        playerService.delete(playerService.findByUserId(0L));
    }

    @Test
    void testFindPlayerById() {
        Player player = playerService.findById(10L);

        Assertions.assertEquals(10L, player.getId());
        Assertions.assertEquals(10L, player.getGame().getId());
        Assertions.assertEquals(0L, player.getUser().getId());
    }

}
