package org.themarioga.game.cah.service.model;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.RoundStatusEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.card.CardAlreadyPlayedException;
import org.themarioga.game.cah.exceptions.card.CardDoesntExistsException;
import org.themarioga.game.cah.exceptions.card.CardNotPlayedException;
import org.themarioga.game.cah.exceptions.player.PlayerAlreadyPlayedCardException;
import org.themarioga.game.cah.exceptions.player.PlayerAlreadyVotedCardException;
import org.themarioga.game.cah.exceptions.player.PlayerCannotVoteCardException;
import org.themarioga.game.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.cah.services.intf.model.CardService;
import org.themarioga.game.cah.services.intf.model.GameService;
import org.themarioga.game.cah.services.intf.model.PlayerService;
import org.themarioga.game.cah.services.intf.model.RoundService;
import org.themarioga.game.commons.services.intf.RoomService;
import org.themarioga.game.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/round.xml")
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
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/model/testDeleteRound-expected.xml", table = "Round", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    void testAddCardToPlayedCards_PlayerAlreadyPlayed() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000004"));

        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Assertions.assertThrows(PlayerAlreadyPlayedCardException.class, () -> roundService.addCardToPlayedCards(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    void testAddCardToPlayedCards_CardAlreadyPlayed() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.PLAYING);

        Assertions.assertThrows(CardAlreadyPlayedException.class, () -> roundService.addCardToPlayedCards(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    void testVoteCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Round round = roundService.voteCard(game.getCurrentRound(), player, card);

        Assertions.assertEquals(1, round.getVotedCards().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    void testVoteCard_WrongStatus() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        Assertions.assertThrows(RoundWrongStatusException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    void testVoteCard_CannotVoteCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("77777777-7777-7777-7777-777777777777")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Assertions.assertThrows(PlayerCannotVoteCardException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/model/votedcard.xml")
    void testVoteCard_AlreadyVotedCard() {
        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        Player player = playerService.findPlayerByGameAndUser(game, userService.getById(UUID.fromString("88888888-8888-8888-8888-888888888888")));
        Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        game.getCurrentRound().setStatus(RoundStatusEnum.VOTING);

        Assertions.assertThrows(PlayerAlreadyVotedCardException.class, () -> roundService.voteCard(game.getCurrentRound(), player, card));
    }

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
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
	@DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/model/votedcard.xml")
	void testGetMostVotedCard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		VotedCard votedCard = roundService.getMostVotedCard(game.getCurrentRound());

		Assertions.assertNotNull(votedCard);
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/model/votedcard.xml")
	void testGetPlayedCardByCard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		VotedCard votedCard = roundService.getMostVotedCard(game.getCurrentRound());

		PlayedCard playedCard = roundService.getPlayedCardByCard(game.getCurrentRound(), votedCard.getCard());

		Assertions.assertNotNull(playedCard);
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/model/votedcard.xml")
	void testGetCheckIfEveryoneHavePlayedACard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		Assertions.assertTrue(roundService.checkIfEveryoneHavePlayedACard(game.getCurrentRound()));
	}

	@Test
	@DatabaseSetup("classpath:dbunit/service/setup/model/playedcard.xml")
	@DatabaseSetup("classpath:dbunit/service/setup/model/votedcard.xml")
	void testGetCheckIfEveryoneHaveVotedACard() {
		Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));

		Assertions.assertTrue(roundService.checkIfEveryoneHaveVotedACard(game.getCurrentRound()));
	}

}
