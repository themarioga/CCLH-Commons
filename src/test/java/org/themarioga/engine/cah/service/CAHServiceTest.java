package org.themarioga.engine.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.enums.RoundStatusEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.exceptions.round.RoundPresidentCannotPlayCardException;
import org.themarioga.engine.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.services.intf.CAHService;
import org.themarioga.engine.cah.services.intf.dictionaries.CardService;
import org.themarioga.engine.cah.services.intf.dictionaries.DictionaryService;
import org.themarioga.engine.cah.services.intf.game.GameService;
import org.themarioga.engine.commons.enums.GameStatusEnum;
import org.themarioga.engine.commons.exceptions.game.GameCreatorCannotLeaveException;
import org.themarioga.engine.commons.exceptions.game.GameDoesntExistsException;
import org.themarioga.engine.commons.exceptions.game.GameNotStartedException;
import org.themarioga.engine.commons.exceptions.game.GameOnlyCreatorCanPerformActionException;
import org.themarioga.engine.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.engine.commons.services.intf.RoomService;
import org.themarioga.engine.commons.services.intf.UserService;
import org.themarioga.engine.commons.util.SessionUtil;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionarycollaborators.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/cah.xml")
class CAHServiceTest extends BaseTest {

    @Autowired
    CAHService cahService;
    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    GameService gameService;
    @Autowired
    CardService cardService;

    @BeforeEach
    void setUpUser() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    @Test
    void testCreateGame_NewRoom() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));

        Game game = cahService.createGame("New Room");

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertEquals("New Room", game.getRoom().getName());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getCreator().getId());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getPlayers().get(0).getUser().getId());
    }

    @Test
    void testCreateGame_ReactivateRoom() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));

        Game game = cahService.createGame("Third");

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), game.getRoom().getId());
        Assertions.assertEquals("Third", game.getRoom().getName());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getCreator().getId());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getPlayers().get(0).getUser().getId());
    }

    @Test
    void testCreateGame_ExistingRoom() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));

        Game game = cahService.createGame("Second");

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.getId());
        Assertions.assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), game.getRoom().getId());
        Assertions.assertEquals("Second", game.getRoom().getName());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getCreator().getId());
        Assertions.assertEquals(UUID.fromString("88888888-8888-8888-8888-888888888888"), game.getPlayers().get(0).getUser().getId());
    }

    @Test
    void testSetVotationMode() {
        Game game = cahService.setVotationMode(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), VotationModeEnum.DICTATORSHIP);

        Assertions.assertEquals(VotationModeEnum.DICTATORSHIP, game.getVotationMode());
    }

    @Test
    void testSetVotationMode_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.setVotationMode(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), VotationModeEnum.DICTATORSHIP));
    }

    @Test
    void testSetVotationMode_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.setVotationMode(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), VotationModeEnum.DICTATORSHIP));
    }

    @Test
    void testSetMaxNumberOfPlayers() {
        Game game = cahService.setMaxNumberOfPlayers(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6);

        Assertions.assertEquals(6, game.getMaxNumberOfPlayers());
    }

    @Test
    void testSetMaxNumberOfPlayers_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.setMaxNumberOfPlayers(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), 6));
    }

    @Test
    void testSetMaxNumberOfPlayers_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.setMaxNumberOfPlayers(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6));
    }

    @Test
    void testSetNumberOfPointsToWin() {
        Game game = cahService.setNumberOfPointsToWin(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6);

        Assertions.assertEquals(6, game.getNumberOfPointsToWin());
    }

    @Test
    void testSetNumberOfPointsToWin_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.setNumberOfPointsToWin(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), 6));
    }

    @Test
    void testSetNumberOfPointsToWin_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.setNumberOfPointsToWin(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6));
    }

    @Test
    void testSetNumberOfRoundsToEnd() {
        Game game = cahService.setNumberOfRoundsToEnd(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6);

        Assertions.assertEquals(6, game.getNumberOfRoundsToEnd());
    }

    @Test
    void testSetNumberOfRoundsToEnd_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.setNumberOfRoundsToEnd(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), 6));
    }

    @Test
    void testSetNumberOfRoundsToEnd_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.setNumberOfRoundsToEnd(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 6));
    }

    @Test
    void testSetDictionary() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        Game game = cahService.setDictionary(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), dictionary);

        Assertions.assertEquals(dictionary.getId(), game.getDictionary().getId());
    }

    @Test
    void testSetDictionary_GameDoesntExistsException() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.setDictionary(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), dictionary));
    }

    @Test
    void testSetDictionary_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.setDictionary(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), dictionary));
    }

    @Test
    void testDeleteGameByCreator() {
        Game game = cahService.deleteGameByCreator(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getCreator().getId());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), game.getRoom().getId());
    }

    @Test
    void testDeleteGameByCreator_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.deleteGameByCreator(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    void testDeleteGameByCreator_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.deleteGameByCreator(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testAddPlayer() {
        cahService.setMaxNumberOfPlayers(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), 4);

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Game game = cahService.addPlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(4, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.addPlayer(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    void testKickPlayer() {
        Game game = cahService.kickPlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(2, game.getPlayers().size());
    }

    @Test
    void testKickPlayer_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.kickPlayer(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
    }

    @Test
    void testKickPlayer_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.kickPlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testKickPlayer_GameCreatorCannotLeaveException() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> cahService.kickPlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testKickPlayer_PlayerDoesntExistsException() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> cahService.kickPlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444"))));
    }

    @Test
    void testLeavePlayer() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Game game = cahService.leavePlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(2, game.getPlayers().size());
    }

    @Test
    void testLeavePlayer_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.leavePlayer(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    void testLeavePlayer_GameCreatorCannotLeaveException() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> cahService.leavePlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testLeavePlayer_PlayerDoesntExistsException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> cahService.leavePlayer(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testVoteForDeletion() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        gameService.setStatus(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))), GameStatusEnum.STARTED);

        Game game = cahService.voteForDeletion(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(1, game.getDeletionVotes().size());
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
    }

    @Test
    void testVoteForDeletion_Ending() {
        gameService.setStatus(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))), GameStatusEnum.STARTED);

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        cahService.voteForDeletion(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Game game = cahService.voteForDeletion(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(2, game.getDeletionVotes().size());
        Assertions.assertEquals(GameStatusEnum.DELETING, game.getStatus());
    }

    @Test
    void testVoteForDeletion_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.voteForDeletion(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    void testVoteForDeletion_PlayerDoesntExistsException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> cahService.voteForDeletion(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testStartGame() {
        Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertNotNull(game);
        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
        Assertions.assertEquals(2, game.getBlackCardsDeck().size());
        Assertions.assertEquals(6, game.getWhiteCardsDeck().size());
        Assertions.assertEquals(3, game.getPlayers().get(0).getHand().size());
    }

    @Test
    void testStartGame_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.startGame(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"))));
    }

    @Test
    void testStartGame_GameOnlyCreatorCanPerformActionException() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testPlayCard() {
        Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        game = cahService.playCard(game.getRoom(), game.getPlayers().get(1).getHand().get(0).getCard());

        Assertions.assertNotNull(game);
        Assertions.assertEquals(1, game.getCurrentRound().getPlayedCards().size());
        Assertions.assertEquals(2, game.getPlayers().get(1).getHand().size());
    }

    @Test
    void testPlayCard_Voting() {
        Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        game = cahService.playCard(game.getRoom(), game.getPlayers().get(1).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        game = cahService.playCard(game.getRoom(), game.getPlayers().get(2).getHand().get(0).getCard());

        Assertions.assertNotNull(game);
        Assertions.assertEquals(2, game.getCurrentRound().getPlayedCards().size());
        Assertions.assertEquals(2, game.getPlayers().get(1).getHand().size());
        Assertions.assertEquals(2, game.getPlayers().get(2).getHand().size());
        Assertions.assertEquals(RoundStatusEnum.VOTING, game.getCurrentRound().getStatus());
    }

    @Test
    void testPlayCard_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.playCard(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testPlayCard_GameNotStartedException() {
        Assertions.assertThrows(GameNotStartedException.class, () -> cahService.playCard(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testPlayCard_PlayerDoesntExistsException() {
        cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("99999999-9999-9999-9999-999999999999")));

        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> cahService.playCard(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testPlayCard_RoundPresidentCannotPlayCardException() {
        cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        Assertions.assertThrows(RoundPresidentCannotPlayCardException.class, () -> cahService.playCard(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testVoteCard() {
        Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(1).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(2).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        cahService.voteCard(game.getRoom(), game.getCurrentRound().getPlayedCards().get(1).getCard());

        Assertions.assertEquals(1, game.getCurrentRound().getVotedCards().size());
        Assertions.assertEquals(RoundStatusEnum.ENDING, game.getCurrentRound().getStatus());
    }

    @Test
    void testVoteCard_GameDoesntExistsException() {
        Assertions.assertThrows(GameDoesntExistsException.class, () -> cahService.voteCard(roomService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testVoteCard_GameNotStartedException() {
        Assertions.assertThrows(GameNotStartedException.class, () -> cahService.voteCard(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testVoteCard_PlayerDoesntExistsException() {
        cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("99999999-9999-9999-9999-999999999999")));

        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> cahService.voteCard(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")), cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
    }

    @Test
    void testNextRound() {
        Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(1).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(2).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        cahService.voteCard(game.getRoom(), game.getCurrentRound().getPlayedCards().get(1).getCard());

	    Assertions.assertEquals(0, game.getCurrentRound().getRoundNumber());

        cahService.nextRound(game);

        Assertions.assertEquals(1, game.getCurrentRound().getRoundNumber());
    }

	@Test
	void testNextRound_GameNotStarted() {
		Assertions.assertThrows(GameNotStartedException.class, () -> cahService.nextRound(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")))));
	}

	@Test
	void testNextRound_GameOnlyCreatorCanPerformActionException() {
		Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

		SessionUtil.setCurrentUser(userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111")));

		Assertions.assertThrows(GameOnlyCreatorCanPerformActionException.class, () -> cahService.nextRound(game));
	}

	@Test
	void testNextRound_RoundWrongStatusException() {
		Game game = cahService.startGame(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")));

		Assertions.assertThrows(RoundWrongStatusException.class, () -> cahService.nextRound(game));
	}

    @Test
    @Disabled
    void completeClassicGameTest() {
        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        Game game = cahService.createGame("Classic Game");

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("55555555-5555-5555-5555-555555555555")));

        cahService.addPlayer(game.getRoom());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("66666666-6666-6666-6666-666666666666")));

        cahService.addPlayer(game.getRoom());

        Assertions.assertEquals(3, game.getPlayers().size());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("44444444-4444-4444-4444-444444444444")));

        cahService.startGame(game.getRoom());

        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("55555555-5555-5555-5555-555555555555")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(1).getHand().get(0).getCard());

        SessionUtil.setCurrentUser(userService.getById(UUID.fromString("66666666-6666-6666-6666-666666666666")));

        cahService.playCard(game.getRoom(), game.getPlayers().get(2).getHand().get(0).getCard());

        Assertions.assertEquals(2, game.getCurrentRound().getPlayedCards().size());
        Assertions.assertEquals(3, game.getCurrentRound().getPlayedCards().size());
        Assertions.assertEquals(RoundStatusEnum.VOTING, game.getCurrentRound().getStatus());
    }

}
