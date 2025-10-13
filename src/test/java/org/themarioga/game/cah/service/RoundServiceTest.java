package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.RoundStatusEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Round;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.RoundService;
import org.themarioga.game.commons.services.intf.RoomService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player2.xml")
@DatabaseSetup("classpath:dbunit/service/setup/deckcard.xml")
@DatabaseSetup("classpath:dbunit/service/setup/round.xml")
class RoundServiceTest extends BaseTest {

    @Autowired
    RoundService roundService;

    @Autowired
    GameService gameService;

    @Autowired
    RoomService roomService;

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
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteRound-expected.xml", table = "Round", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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

}
