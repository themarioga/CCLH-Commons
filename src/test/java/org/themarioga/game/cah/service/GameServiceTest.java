package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.GameAlreadyExistsException;
import org.themarioga.game.commons.exceptions.game.GameAlreadyStartedException;
import org.themarioga.game.commons.exceptions.game.GameCreatorCannotLeaveException;
import org.themarioga.game.commons.exceptions.game.GameNotStartedException;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.game.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.game.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.game.commons.exceptions.user.UserNotActiveException;
import org.themarioga.game.commons.models.Game;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class GameServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateGame() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Game game = gameService.create("Room 3", creator);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertNotNull(game.getRoom().getId());
        Assertions.assertNotNull(game.getCreator().getId());

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void testCreate_GameAlreadyExists() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create("Room 3", creator));
    }

    @Test
    void testCreate_CreatorAlreadyHaveGame() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create("Room 3", creator));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testDeleteGame() {
        gameService.delete(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertNull(game);
    }

    @Test
    void testDelete_RoomNotExists() {
        Assertions.assertThrows(RoomDoesntExistsException.class, () -> gameService.delete(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testDelete_GameNotExists() {
        Assertions.assertThrows(ApplicationException.class, () -> gameService.delete(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testAddPlayer() {
        org.themarioga.game.cah.models.Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        gameService.addPlayer(game, playerService.create(game, creator));

        Assertions.assertEquals(1L, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_UserNotActive() {
        Assertions.assertThrows(UserNotActiveException.class, () -> gameService.addPlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeaveGame() {
        Game game = gameService.removePlayer(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testLeaveGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeaveGame_CreatorLeave() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeaveGame_PlayerNotInGame() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testStartGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testEndGame() {
        Game game = gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());

        game = gameService.endGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.ENDING, game.getStatus());
    }

    @Test
    void testVoteDeletion_vote() {
        gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        Game game = gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getDeletionVotes().get(0).getId());
    }

    @Test
    void testVoteDeletion_delete() {
        gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        Game game = gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    void testLeaveGame_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testVoteDeletion_PlayerAlreadyVoted() {
        gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertThrows(PlayerAlreadyVotedDeleteException.class, () -> gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxNumberOfPlayers() {
//        Table table = tableService.setMaxNumberOfPlayers(tableService.getByRoomId(0L), 5);
//
//        getCurrentSession().flush();
//
//        Assertions.assertEquals(5, table.getMaxNumberOfPlayers());
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyStarted() {
//        Assertions.assertThrows(GameAlreadyStartedException.class, () -> tableService.setMaxNumberOfPlayers(tableService.getByRoomId(3L), 5));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/roundplayedcards.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/roundplayervotes.xml")
    void testGetMostVotedCard() {
//        PlayedCard mostVotedCard = gameService.get(10L);
//
//        Assertions.assertNotNull(mostVotedCard);
    }

    @Test
    void testAddPlayer_GameAlreadyFilled() {
//        Assertions.assertThrows(GameAlreadyFilledException.class, () -> tableService.addPlayer(tableService.getByRoomId(0L), playerService.findByUserId(3L)));
    }

    @Test
    void testStartGame_GameNotFilled() {
//        Assertions.assertThrows(GameNotFilledException.class, () -> tableService.startGame(tableService.getByRoomId(1L)));
    }

}
