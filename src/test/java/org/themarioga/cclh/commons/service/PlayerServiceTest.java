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
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.*;

import java.util.ArrayList;
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
    @Autowired
    CardService cardService;

    @Test
    void testCreate() {
        Game game = gameService.getByRoom(roomService.getById(0L));

        Player player = playerService.create(game, 4L);

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
    void testDelete() {
        playerService.delete(playerService.findByUserId(0L));
    }

    @Test
    void testIncreasePoints() {
        Player player = playerService.findByUserId(0L);

        Assertions.assertEquals(0, player.getPoints());

        playerService.incrementPoints(player);

        Assertions.assertEquals(1, player.getPoints());
    }

    @Test
    void testFindPlayerById() {
        Player player = playerService.findById(10L);

        Assertions.assertEquals(10L, player.getId());
        Assertions.assertEquals(10L, player.getGame().getId());
        Assertions.assertEquals(0L, player.getUser().getId());
    }

}
