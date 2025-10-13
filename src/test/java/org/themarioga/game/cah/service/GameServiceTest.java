package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.game.GameAlreadyFilledException;
import org.themarioga.game.cah.exceptions.game.GameNotFilledException;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.Round;
import org.themarioga.game.cah.services.intf.DictionaryService;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.PlayerService;
import org.themarioga.game.cah.services.intf.RoundService;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.*;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.game.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.RoomService;
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
    private RoomService roomService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RoundService roundService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateGame() {
        Room room = roomService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        User creator = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));

        Game game = gameService.create(room, creator);
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
        Room room = roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User creator = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));

        Assertions.assertThrows(GameAlreadyExistsException.class, () -> gameService.create(room, creator));
    }

    @Test
    void testCreate_CreatorAlreadyHaveGame() {
        Room room = roomService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameCreatorAlreadyExistsException.class, () -> gameService.create(room, creator));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameVotationMode-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testUpdate() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game.setVotationMode(VotationModeEnum.DICTATORSHIP);
        game = gameService.update(game);

        getCurrentSession().flush();

        Assertions.assertNotNull(game);
        Assertions.assertEquals(VotationModeEnum.DICTATORSHIP, game.getVotationMode());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testDeleteGame() {
        gameService.delete(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));

        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNull(game);
    }

    @Test
    void testDelete_RoomNotExists() {
        Assertions.assertThrows(RoomDoesntExistsException.class, () -> gameService.delete(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000001")))));
    }

    @Test
    void testDelete_GameNotExists() {
        Assertions.assertThrows(ApplicationException.class, () -> gameService.delete(gameService.getByRoom(roomService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")))));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameStatus-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testUpdateStatus() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game = gameService.setStatus(game, GameStatusEnum.DELETING);
        getCurrentSession().flush();

        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameVotationMode-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxVotationMode() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game = gameService.setVotationMode(game, VotationModeEnum.DICTATORSHIP);
        getCurrentSession().flush();

        Assertions.assertEquals(VotationModeEnum.DICTATORSHIP, game.getVotationMode());
    }

    @Test
    void testSetMaxVotationMode_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setVotationMode(game, VotationModeEnum.DICTATORSHIP));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxNumberOfPlayers() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game = gameService.setMaxNumberOfPlayers(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(5, game.getMaxNumberOfPlayers());
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setMaxNumberOfPlayers(game, 5));
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyFilled() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.setMaxNumberOfPlayers(game, 1));
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyFilled2() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.setMaxNumberOfPlayers(game, 1));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPoints-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetNumberPointsToWin() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game = gameService.setNumberOfPointsToWin(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(PunctuationModeEnum.POINTS, game.getPunctuationMode());
        Assertions.assertEquals(5, game.getNumberOfPointsToWin());
    }

    @Test
    void testSetNumberPointsToWin_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setNumberOfPointsToWin(game, 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberRounds-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetNumberRounds() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        game = gameService.setNumberOfRoundsToEnd(game, 5);
        getCurrentSession().flush();

        Assertions.assertEquals(PunctuationModeEnum.ROUNDS, game.getPunctuationMode());
        Assertions.assertEquals(5, game.getNumberOfRounds());
    }

    @Test
    void testSetNumberRounds_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setNumberOfRoundsToEnd(game, 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameDictionary-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetDictionary() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        game = gameService.setDictionary(game, dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(dictionary, game.getDictionary());
    }

    @Test
    void testSetDictionary_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setDictionary(game, dictionary));
    }

    @Test
    void testAddPlayer() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));
        User user = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        Player player = playerService.create(game, user);
        gameService.addPlayer(game, player);

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_GameStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        User user = userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        Player player = playerService.create(game, user);

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.addPlayer(game, player));
    }

    @Test
    void testAddPlayer_GameAlreadyFilled() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777"));
        Player player = playerService.create(game, user);

        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.addPlayer(game, player));
    }

    @Test
    void testRemovePlayer() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        game = gameService.removePlayer(game, player);

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testRemovePlayer_GameAlreadyStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.removePlayer(game, player));
    }

    @Test
    void testRemovePlayer_CreatorLeave() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> gameService.removePlayer(game, player));
    }

    @Test
    void testStartGame() {
        Game game = gameService.startGame(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    void testStartGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.startGame(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")))));
    }

    @Test
    void testStartGame_GameNotFilled() {
        Assertions.assertThrows(GameNotFilledException.class, () -> gameService.startGame(gameService.getByRoom(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testStartGame_GameOverflowed() {
        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.startGame(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_vote() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        User user = userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        game = gameService.voteForDeletion(game, player);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("77777777-7777-7777-7777-777777777777"), game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testVoteDeletion_delete() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        User user = userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        game = gameService.voteForDeletion(game, player);

        user = userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888"));
        player = playerService.findPlayerByGameAndUser(game, user);

        game = gameService.voteForDeletion(game, player);

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getDeletionVotes().get(0));
        Assertions.assertEquals(UUID.fromString("77777777-7777-7777-7777-777777777777"), game.getDeletionVotes().get(0).getId());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getDeletionVotes().get(1).getId());
        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    void testVoteForDeletion_GameNotStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.voteForDeletion(game, player));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/gamedeletionvotes.xml")
    void testVoteDeletion_PlayerAlreadyVoted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player = playerService.findPlayerByGameAndUser(game, user);

        game.setStatus(GameStatusEnum.STARTED);

        Assertions.assertThrows(PlayerAlreadyVotedDeleteException.class, () -> gameService.voteForDeletion(game, player));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game_endGame.xml")
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testEndGame-expected.xml", table = "Game", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testEndGame() {
        gameService.endGame(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));

        Assertions.assertNull(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
    }

    @Test
    void testEndGame_GameNotEnding() {
        Assertions.assertThrows(GameNotEndingException.class, () -> gameService.endGame(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")))));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/deckcard.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testSetCurrentRound() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Round round = roundService.createRound(game, 0);
        game = gameService.setCurrentRound(game, round);

        Assertions.assertEquals(round, game.getCurrentRound());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/deckcard.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
    void testSetCurrentRound_GameNotStarted() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        game.setStatus(GameStatusEnum.DELETING);
        Round round = roundService.createRound(game, 0);

        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.setCurrentRound(game, round));
    }

    @Test
    void testGetNextRoundNumber_FirstRound() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertEquals(0, gameService.getNextRoundNumber(game));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/round.xml")
    void testGetNextRoundNumber_SecondRound() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertEquals(1, gameService.getNextRoundNumber(game));
    }

}
