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

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
@DatabaseSetup("classpath:dbunit/service/setup/table.xml")
class GameServiceTest extends BaseTest {

    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "CAHGame", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateGame() {
        Game game = gameService.create(2L, "Room 3", 3L);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertNotNull(game.getRoom().getId());
        Assertions.assertNotNull(game.getCreator().getId());

        Assertions.assertEquals(2L, game.getRoom().getId());
        Assertions.assertEquals(3L, game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void testCreate_GameAlreadyExists() {
        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create(1L, "Room 3", 3L));
    }

    @Test
    void testCreate_CreatorAlreadyHaveGame() {
        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create(2L, "Room 3", 1L));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteGame-expected.xml", table = "CAHGame", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testDeleteGame() {
        gameService.delete(gameService.getByRoomId(0L));

        Game game = gameService.getByRoomId(0L);

        Assertions.assertNull(game);
    }

    @Test
    void testDelete_RoomNotExists() {
        Assertions.assertThrows(RoomDoesntExistsException.class, () -> gameService.delete(gameService.getByRoomId(70L)));
    }

    @Test
    void testDelete_GameNotExists() {
        Assertions.assertThrows(ApplicationException.class, () -> gameService.delete(gameService.getByRoomId(4L)));
    }

    @Test
    void testAddPlayer() {
        Game game = gameService.getByRoomId(1L);
        gameService.addPlayer(game, playerService.create(game, 4L));

        Assertions.assertEquals(1L, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_UserNotActive() {
        Assertions.assertThrows(UserNotActiveException.class, () -> gameService.addPlayer(gameService.getByRoomId(0L), playerService.findByUserId(2L)));
    }

    @Test
    void testLeaveGame() {
        Game game = gameService.removePlayer(gameService.getByRoomId(0L), playerService.findByUserId(1L));

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testLeaveGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.removePlayer(gameService.getByRoomId(3L), playerService.findByUserId(0L)));
    }

    @Test
    void testLeaveGame_CreatorLeave() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> gameService.removePlayer(gameService.getByRoomId(0L), playerService.findByUserId(0L)));
    }

    @Test
    void testLeaveGame_PlayerNotInGame() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> gameService.removePlayer(gameService.getByRoomId(1L), playerService.findByUserId(3L)));
    }

    @Test
    void testStartGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.startGame(gameService.getByRoomId(3L)));
    }

    @Test
    void testEndGame() {
        Game game = gameService.startGame(gameService.getByRoomId(0L));

        Assertions.assertEquals(0L, game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());

        game = gameService.endGame(gameService.getByRoomId(0L));

        Assertions.assertEquals(0L, game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.ENDING, game.getStatus());
    }

    @Test
    void testVoteDeletion_vote() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.voteForDeletion(gameService.getByRoomId(0L), 0L);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(10L, game.getDeletionVotes().get(0).getId());
    }

    @Test
    void testVoteDeletion_delete() {
        gameService.startGame(gameService.getByRoomId(0L));
        gameService.voteForDeletion(gameService.getByRoomId(0L), 1L);
        Game game = gameService.voteForDeletion(gameService.getByRoomId(0L), 0L);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(11L, game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    void testLeaveGame_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.voteForDeletion(gameService.getByRoomId(0L), 0L));
    }

    @Test
    void testVoteDeletion_PlayerAlreadyVoted() {
        gameService.startGame(gameService.getByRoomId(0L));
        gameService.voteForDeletion(gameService.getByRoomId(0L), 1L);

        Assertions.assertThrows(PlayerAlreadyVotedDeleteException.class, () -> gameService.voteForDeletion(gameService.getByRoomId(0L), 1L));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
    @DatabaseSetup("classpath:dbunit/service/setup/tableplayedcards.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/tableplayervotes.xml")
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
