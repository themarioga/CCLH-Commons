package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.enums.TableStatusEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.deck.DeckDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.game.*;
import org.themarioga.cclh.commons.exceptions.player.*;
import org.themarioga.cclh.commons.exceptions.room.RoomDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.user.UserNotActiveException;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.services.intf.DeckService;
import org.themarioga.cclh.commons.services.intf.GameService;
import org.themarioga.cclh.commons.services.intf.PlayerService;
import org.themarioga.cclh.commons.services.intf.UserService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
class GameServiceTest extends BaseTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    DeckService deckService;

    @Autowired
    PlayerService playerService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateGame_CreateRoom() {
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
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteGame-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameType-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetType() {
        Game game = gameService.setType(gameService.getByRoomId(0L), GameTypeEnum.DICTATORSHIP);

        getCurrentSession().flush();

        Assertions.assertEquals(GameTypeEnum.DICTATORSHIP, game.getType());
    }

    @Test
    void testSetType_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setType(gameService.getByRoomId(3L), GameTypeEnum.DICTATORSHIP));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberCards-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetNumberOfCardsToWin() {
        Game game = gameService.setNumberOfCardsToWin(gameService.getByRoomId(0L), 5);

        getCurrentSession().flush();

        Assertions.assertEquals(5, game.getNumberOfCardsToWin());
    }

    @Test
    void testSetNumberOfCardsToWin_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setNumberOfCardsToWin(gameService.getByRoomId(3L), 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameNumberPlayers-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetMaxNumberOfPlayers() {
        Game game = gameService.setMaxNumberOfPlayers(gameService.getByRoomId(0L), 5);

        getCurrentSession().flush();

        Assertions.assertEquals(5, game.getMaxNumberOfPlayers());
    }

    @Test
    void testSetMaxNumberOfPlayers_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setMaxNumberOfPlayers(gameService.getByRoomId(3L), 5));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testUpdateGameDictionary-expected.xml", table = "T_GAME", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testSetDictionary() {
        Game game = gameService.setDeck(gameService.getByRoomId(0L), 1L);

        getCurrentSession().flush();

        Assertions.assertEquals(deckService.findOne(1L), game.getDeck());
    }

    @Test
    void testSetMaxDictionary_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.setDeck(gameService.getByRoomId(3L), 0L));
    }

    @Test
    void testSetMaxDictionary_DictionaryDoesntExists() {
        Assertions.assertThrows(DeckDoesntExistsException.class, () -> gameService.setDeck(gameService.getByRoomId(0L), 50L));
    }

    @Test
    void testAddPlayer() {
        Game game = gameService.getByRoomId(1L);
        gameService.addPlayer(game, playerService.create(game, 4L));

        Assertions.assertEquals(1L, game.getPlayers().size());
    }

    @Test
    void testAddPlayer_GameAlreadyFilled() {
        Assertions.assertThrows(GameAlreadyFilledException.class, () -> gameService.addPlayer(gameService.getByRoomId(0L), playerService.findByUserId(3L)));
    }

    @Test
    void testAddPlayer_UserNotActive() {
        Assertions.assertThrows(UserNotActiveException.class, () -> gameService.addPlayer(gameService.getByRoomId(0L), playerService.findByUserId(2L)));
    }

    @Test
    void testLeaveGame() {
        Game game = gameService.leaveGame(gameService.getByRoomId(0L), 1L);

        Assertions.assertEquals(2L, game.getPlayers().size());
    }

    @Test
    void testLeaveGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.leaveGame(gameService.getByRoomId(3L), 0L));
    }

    @Test
    void testLeaveGame_CreatorLeave() {
        Assertions.assertThrows(GameCreatorCannotLeaveException.class, () -> gameService.leaveGame(gameService.getByRoomId(0L), 0L));
    }

    @Test
    void testLeaveGame_PlayerNotInGame() {
        Assertions.assertThrows(PlayerDoesntExistsException.class, () -> gameService.leaveGame(gameService.getByRoomId(0L), 5L));
    }

    @Test
    void testStartGame_Democracy() {
        Game game = gameService.startGame(gameService.getByRoomId(0L));

        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
        Assertions.assertNotNull(game.getTable());
        Assertions.assertEquals(3, game.getTable().getDeck().size());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(3, game.getPlayers().get(0).getDeck().size());
    }

    @Test
    void testStartGame_Dictatorship() {
        Game game = gameService.getByRoomId(1L);

        gameService.addPlayer(game, playerService.create(game, 4L));
        gameService.addPlayer(game, playerService.create(game, 5L));
        gameService.addPlayer(game, playerService.create(game, 6L));

        gameService.startGame(game);

        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
        Assertions.assertNotNull(game.getTable());
        Assertions.assertEquals(3, game.getTable().getDeck().size());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(10L, game.getTable().getCurrentPresident().getId());
    }

    @Test
    void testStartGame_GameAlreadyStarted() {
        Assertions.assertThrows(GameAlreadyStartedException.class, () -> gameService.startGame(gameService.getByRoomId(3L)));
    }

    @Test
    void testStartGame_GameNotFilled() {
        Assertions.assertThrows(GameNotFilledException.class, () -> gameService.startGame(gameService.getByRoomId(1L)));
    }

    @Test
    void testStartRound() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));

        Assertions.assertEquals(0L, game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(3, game.getPlayers().get(0).getHand().size());
        Assertions.assertEquals(1, game.getTable().getCurrentRoundNumber());
        Assertions.assertNotNull(game.getTable().getCurrentBlackCard());
    }

    @Test
    void testStartRound_Classic() {
        gameService.setType(gameService.getByRoomId(0L), GameTypeEnum.CLASSIC);
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));

        Assertions.assertEquals(0L, game.getRoom().getId());
        Assertions.assertEquals(11L, game.getTable().getCurrentPresident().getId());
    }

    @Test
    void testStartRound_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.startRound(gameService.getByRoomId(0L)));
    }

    @Test
    void testEndRound() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));

        Assertions.assertEquals(0L, game.getRoom().getId());
        Assertions.assertEquals(3, game.getPlayers().size());
        Assertions.assertEquals(3, game.getPlayers().get(0).getHand().size());
        Assertions.assertEquals(1, game.getTable().getCurrentRoundNumber());
        Assertions.assertNotNull(game.getTable().getCurrentBlackCard());

        game.getTable().setStatus(TableStatusEnum.ENDING);
        gameDao.update(game);
        gameService.endRound(gameService.getByRoomId(0L));

        Assertions.assertNull(game.getTable().getCurrentBlackCard());
        Assertions.assertEquals(0, game.getTable().getPlayedCards().size());
        Assertions.assertEquals(0, game.getTable().getPlayerVotes().size());
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
    void testPlayCard() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));
        getCurrentSession().flush();
        Player player = game.getPlayers().get(0);
        game = gameService.playCard(game, player.getUser().getId(), player.getHand().get(0).getCard().getId());

        Assertions.assertEquals(1, game.getTable().getPlayedCards().size());
    }

    @Test
    void testPlayCard_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.playCard(gameService.getByRoomId(0L), 0L, 0L));
    }

    @Test
    void testPlayCard_PlayerAlreadyPlayed() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));
        Player player = game.getPlayers().get(0);
        gameService.playCard(game, player.getUser().getId(), player.getHand().get(0).getCard().getId());

        Assertions.assertThrows(PlayerAlreadyPlayedCardException.class, () -> gameService.playCard(game, player.getUser().getId(), player.getHand().get(0).getCard().getId()));
    }

    @Test
    void testPlayCard_PlayerCannotPlayCardException() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));
        Player player = game.getPlayers().get(0);
        gameService.playCard(game, player.getUser().getId(), player.getHand().get(0).getCard().getId());

        Assertions.assertThrows(PlayerCannotPlayCardException.class, () -> gameService.playCard(game, 1L, player.getHand().get(0).getCard().getId()));
    }

    @Test
    void testVoteCard() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));
        Player player = game.getPlayers().get(0);
        Card card = player.getHand().get(0).getCard();
        gameService.playCard(game, player.getUser().getId(), card.getId());
        game.getTable().setStatus(TableStatusEnum.VOTING);
        gameDao.update(game);
        game = gameService.voteForCard(game, player.getUser().getId(), card.getId());

        Assertions.assertEquals(1, game.getTable().getPlayerVotes().size());
    }

    @Test
    void testVoteCard_GameNotStarted() {
        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.voteForCard(gameService.getByRoomId(0L), 0L, 0L));
    }

    @Test
    void testVoteCard_PlayerAlreadyVoted() {
        gameService.startGame(gameService.getByRoomId(0L));
        Game game = gameService.startRound(gameService.getByRoomId(0L));
        Player player = game.getPlayers().get(0);
        Card card = player.getHand().get(0).getCard();
        gameService.playCard(game, player.getUser().getId(), card.getId());
        game.getTable().setStatus(TableStatusEnum.VOTING);
        gameDao.update(game);
        gameService.voteForCard(game, player.getUser().getId(), card.getId());

        Assertions.assertThrows(PlayerAlreadyVotedCardException.class, () -> gameService.voteForCard(game, player.getUser().getId(), card.getId()));
    }

    @Test
    void testVoteCard_PlayerCannotVote() {
        gameService.setType(gameService.getByRoomId(0L), GameTypeEnum.DICTATORSHIP);
        Game game = gameService.startGame(gameService.getByRoomId(0L));
        game.getTable().setStatus(TableStatusEnum.VOTING);
        gameDao.update(game);

        Assertions.assertThrows(PlayerCannotVoteCardException.class, () -> gameService.voteForCard(gameService.getByRoomId(0L), 1L, 0L));
    }

}
