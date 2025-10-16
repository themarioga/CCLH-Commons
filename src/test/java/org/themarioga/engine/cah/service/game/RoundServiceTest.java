package org.themarioga.engine.cah.service.game;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.enums.RoundStatusEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.exceptions.card.CardAlreadyPlayedException;
import org.themarioga.engine.cah.exceptions.card.CardDoesntExistsException;
import org.themarioga.engine.cah.exceptions.card.CardNotPlayedException;
import org.themarioga.engine.cah.exceptions.player.PlayerAlreadyPlayedCardException;
import org.themarioga.engine.cah.exceptions.player.PlayerAlreadyVotedCardException;
import org.themarioga.engine.cah.exceptions.player.PlayerCannotVoteCardException;
import org.themarioga.engine.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.*;
import org.themarioga.engine.cah.services.intf.dictionaries.CardService;
import org.themarioga.engine.cah.services.intf.game.GameService;
import org.themarioga.engine.cah.services.intf.game.PlayerService;
import org.themarioga.engine.cah.services.intf.game.RoundService;
import org.themarioga.engine.commons.services.intf.RoomService;
import org.themarioga.engine.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/player2.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/deckcard.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game/round.xml")
class RoundServiceTest extends BaseTest {

    @Autowired
    RoundService roundService;

    @Autowired
    GameService gameService;

    @Autowired
    RoomService roomService;

    @Autowired
    PlayerService playerService;

    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;

    @Test
    void testCreateRound_Classic() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Round round = roundService.createRound(game, 0);

        Assertions.assertNotNull(round);
        Assertions.assertEquals(0, round.getRoundNumber());
        Assertions.assertNotNull(round.getRoundPresident());
    }

    @Test
    void testCreateRound_Democracy() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        game.setVotationMode(VotationModeEnum.DEMOCRACY);

        Round round = roundService.createRound(game, 1);

        Assertions.assertNotNull(round);
        Assertions.assertEquals(1, round.getRoundNumber());
        Assertions.assertNull(round.getRoundPresident());
    }

    @Test
    void testCreateRound_Dictatorship() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        game.setVotationMode(VotationModeEnum.DICTATORSHIP);

        Round round = roundService.createRound(game, 0);

        Assertions.assertNotNull(round);
        Assertions.assertEquals(0, round.getRoundNumber());
        Assertions.assertNotNull(round.getRoundPresident());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/game/testDeleteRound-expected.xml", table = "Round", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testDeleteRound() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

        Assertions.assertNotNull(game.getCurrentRound());

        roundService.deleteRound(game.getCurrentRound());
        getCurrentSession().flush();
    }

    @Test
    void testDeleteRound_NotEnding() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Assertions.assertThrows(RoundWrongStatusException.class, () -> roundService.deleteRound(game.getCurrentRound()));
    }

	@Test
	void testSetRound() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		Round round = roundService.setStatus(game.getCurrentRound(), RoundStatusEnum.PLAYING);

		Assertions.assertEquals(RoundStatusEnum.PLAYING, round.getStatus());
	}

    @Test
    void testAddCardToPlayedCards() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Round round = roundService.addCardToPlayedCards(game.getCurrentRound(), player, card);

        Assertions.assertEquals(1, round.getPlayedCards().size());
    }

    @Test
    void testAddCardToPlayedCards_WrongStatus() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        Assertions.assertThrows(RoundWrongStatusException.class, () -> roundService.addCardToPlayedCards(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testAddCardToPlayedCards_PlayerAlreadyPlayed() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000004"));

        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Assertions.assertThrows(PlayerAlreadyPlayedCardException.class, () -> roundService.addCardToPlayedCards(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testAddCardToPlayedCards_CardAlreadyPlayed() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Assertions.assertThrows(CardAlreadyPlayedException.class, () -> roundService.addCardToPlayedCards(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testVoteCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Round round = roundService.voteCard(game.getCurrentRound(), player, card);

        Assertions.assertEquals(1, round.getVotedCards().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testVoteCard_WrongStatus() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        Assertions.assertThrows(RoundWrongStatusException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testVoteCard_CannotVoteCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Assertions.assertThrows(PlayerCannotVoteCardException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/game/votedcard.xml")
    void testVoteCard_AlreadyVotedCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Assertions.assertThrows(PlayerAlreadyVotedCardException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
    void testVoteCard_CardNotPlayed() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Assertions.assertThrows(CardNotPlayedException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

	@Test
	void testSetNextBlackCard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Round round = roundService.setNextBlackCard(game.getCurrentRound(), card);

		Assertions.assertEquals(card.getId(), round.getRoundBlackCard().getId());
	}

	@Test
	void testSetNextBlackCard_CardDoesntExists() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000004"));

		Assertions.assertThrows(CardDoesntExistsException.class, () -> roundService.setNextBlackCard(game.getCurrentRound(), card));
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/game/votedcard.xml")
	void testGetMostVotedCard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		VotedCard votedCard = roundService.getMostVotedCard(game.getCurrentRound());

		Assertions.assertNotNull(votedCard);
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/game/votedcard.xml")
	void testGetPlayedCardByCard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		VotedCard votedCard = roundService.getMostVotedCard(game.getCurrentRound());

		PlayedCard playedCard = roundService.getPlayedCardByCard(game.getCurrentRound(), votedCard.getCard());

		Assertions.assertNotNull(playedCard);
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/game/votedcard.xml")
	void testGetCheckIfEveryoneHavePlayedACard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		Assertions.assertTrue(roundService.checkIfEveryoneHavePlayedACard(game.getCurrentRound()));
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/game/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/game/votedcard.xml")
	void testGetCheckIfEveryoneHaveVotedACard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		Assertions.assertTrue(roundService.checkIfEveryoneHaveVotedACard(game.getCurrentRound()));
	}

}
