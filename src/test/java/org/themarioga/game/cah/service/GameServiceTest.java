package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.game.GameAlreadyFilledException;
import org.themarioga.game.cah.exceptions.game.GameNotFilledException;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.services.intf.DictionaryService;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.*;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.game.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.game.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.game.commons.exceptions.user.UserNotActiveException;
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
    DictionaryService dictionaryService;
    @Autowired
    GameService gameService;
    @Autowired
    PlayerService playerService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateGame() {
        User creator = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));

        Game game = gameService.create("Fifth", creator);
        getCurrentSession().flush();

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertNotNull(game.getRoom().getId());
        Assertions.assertNotNull(game.getCreator().getId());

        Assertions.assertEquals(UUID.fromString("44444444-4444-4444-4444-444444444444"), game.getRoom().getId());
        Assertions.assertEquals(UUID.fromString("44444444-4444-4444-4444-444444444444"), game.getCreator().getId());
        Assertions.assertEquals(GameStatusEnum.CREATED, game.getStatus());
    }

    @Test
    void testCreate_GameAlreadyExists() {
        User creator = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));

        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create("First", creator));
    }

    @Test
    void testCreate_CreatorAlreadyHaveGame() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameCreatorAlreadyExistsException.class, () -> gameService.create("Room 3", creator));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameVotationMode-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxVotationMode() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        game = gameService.setVotationMode(game, VotationModeEnum.DICTATORSHIP);
        getCurrentSession().flush();

        Assertions.assertEquals(VotationModeEnum.DICTATORSHIP, game.getVotationMode());
    }

    @Test
    void testSetMaxVotationMode_GameAlreadyStarted() {
        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setVotationMode(game, VotationModeEnum.DICTATORSHIP));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxNumberOfPlayers() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        game = gameService.setMaxNumberOfPlayers(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(5, game.getMaxNumberOfPlayers());
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyStarted() {
        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setMaxNumberOfPlayers(game, 5));
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyFilled() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.setMaxNumberOfPlayers(game, 1));
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyFilled2() {
        Game game = gameService.getByRoomId(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.setMaxNumberOfPlayers(game, 1));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPoints-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetNumberPointsToWin() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        game = gameService.setNumberOfPointsToWin(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(PunctuationModeEnum.POINTS, game.getPunctuationMode());
        Assertions.assertEquals(5, game.getNumberOfPointsToWin());
    }

    @Test
    void testSetNumberPointsToWin_GameAlreadyStarted() {
        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setNumberOfPointsToWin(game, 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberRounds-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetNumberRounds() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        game = gameService.setNumberOfRoundsToEnd(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(PunctuationModeEnum.ROUNDS, game.getPunctuationMode());
        Assertions.assertEquals(5, game.getNumberOfRounds());
    }

    @Test
    void testSetNumberRounds_GameAlreadyStarted() {
        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setNumberOfRoundsToEnd(game, 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameDictionary-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetDictionary() {
        Game game = gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        game = gameService.setDictionary(game, dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(dictionary, game.getDictionary());
    }

    @Test
    void testSetDictionary_GameAlreadyStarted() {
        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setDictionary(game, dictionary));
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
        Assertions.assertThrows(RoomDoesntExistsException.class, () -> gameService.delete(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000001"))));
    }

    @Test
    void testDelete_GameNotExists() {
        Assertions.assertThrows(ApplicationException.class, () -> gameService.delete(gameService.getByRoomId(UUID.fromString("44444444-4444-4444-4444-444444444444"))));
    }

    @Test
    void testAddPlayer() {
        Game game = gameService.getByRoomId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        User creator = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        gameService.addPlayer(game, playerService.create(game, creator));

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_UserNotActive() {
        Assertions.assertThrows(UserNotActiveException.class, () -> gameService.addPlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("22222222-2222-2222-2222-222222222222"))));
    }

    @Test
    void testAddPlayer_GameAlreadyFilled() {
        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.addPlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("66666666-6666-6666-6666-666666666666"))));
    }

    @Test
    void testLeaveGame() {
        Game game = gameService.removePlayer(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testLeaveGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeaveGame_CreatorLeave() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeaveGame_PlayerNotInGame() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> gameService.removePlayer(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findByUserId(UUID.fromString("66666666-6666-6666-6666-666666666666"))));
    }

    @Test
    void testLeaveGame_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testStartGame() {
        Game game = gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    void testStartGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.startGame(gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
    }

    @Test
    void testStartGame_GameNotFilled() {
        Assertions.assertThrows(GameNotFilledException.class, () -> gameService.startGame(gameService.getByRoomId(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testStartGame_GameOverflowed() {
        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.startGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_vote() {
        Game game = gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("44444444-4444-4444-4444-444444444444"), game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_delete() {
        gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("44444444-4444-4444-4444-444444444444")));
        Game game = gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("55555555-5555-5555-5555-555555555555")));

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("44444444-4444-4444-4444-444444444444"), game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(UUID.fromString("55555555-5555-5555-5555-555555555555"), game.getDeletionVotes().get(1).getId());
        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_PlayerAlreadyVoted() {
        gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Assertions.assertThrows(PlayerAlreadyVotedDeleteException.class, () -> gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("44444444-4444-4444-4444-444444444444"))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_PlayerIsFromAnotherGame() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> gameService.voteForDeletion(
                gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")), playerService.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game2.xml")
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testEndGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testEndGame() {
        gameService.endGame(gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Game game = gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertNull(game);
    }

    @Test
    void testEndGame_GameNotEnding() {
        Assertions.assertThrows(GameNotEndingException.class, () -> gameService.endGame(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    @Disabled
    void testStartRound_FirstRound() {
        Game game = gameService.startRound(gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    @Disabled
    void testStartRound_SecondRound() {
        Game game = gameService.startRound(gameService.getByRoomId(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    void testStartRound_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.startRound(gameService.getByRoomId(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

}
