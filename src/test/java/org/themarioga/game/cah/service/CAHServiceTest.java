package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.services.intf.model.GameService;
import org.themarioga.game.cah.services.intf.model.PlayerService;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
class CAHServiceTest extends BaseTest {

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Test
    @Disabled
    void completeTableTest() {
//        Game game = gameService.create(0L, "Habitación", 0L);
//        gameService.setVotationMode(game, VotationModeEnum.DEMOCRACY);
//        gameService.setNumberOfPointsToWin(game, 3);
//        gameService.setDictionary(game, 0L);
//        tableService.addPlayer(table, playerService.create(table, 0L));
//        tableService.addPlayer(table, playerService.create(table, 1L));
//        tableService.addPlayer(table, playerService.create(table, 3L));
//        tableService.startGame(table);
//        getCurrentSession().flush();
//        getCurrentSession().refresh(game);
//        gameService.startRound(game);
//        tableService.playCard(table, 0L, table.getGame().getPlayers().get(0).getHand().get(0).getCard().getId());
//        tableService.playCard(table, 1L, table.getGame().getPlayers().get(1).getHand().get(0).getCard().getId());
//        tableService.playCard(table, 3L, table.getGame().getPlayers().get(2).getHand().get(0).getCard().getId());
//        tableService.voteForCard(table, 0L, table.getPlayedCards().get(0).getCard().getId());
//        tableService.voteForCard(table, 1L, table.getPlayedCards().get(0).getCard().getId());
//        tableService.voteForCard(table, 3L, table.getPlayedCards().get(0).getCard().getId());
//        gameService.endRound(game);
//        gameService.startRound(game);
//        tableService.playCard(table, 0L, table.getGame().getPlayers().get(0).getHand().get(0).getCard().getId());
//        tableService.playCard(table, 1L, table.getGame().getPlayers().get(1).getHand().get(0).getCard().getId());
//        tableService.playCard(table, 3L, table.getGame().getPlayers().get(2).getHand().get(0).getCard().getId());
//        tableService.voteForCard(table, 0L, table.getPlayedCards().get(0).getCard().getId());
//        tableService.voteForCard(table, 1L, table.getPlayedCards().get(0).getCard().getId());
//        tableService.voteForCard(table, 3L, table.getPlayedCards().get(0).getCard().getId());
//        gameService.endRound(game);
    }


//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/playerhand2.xml")
//    void testStartRound_FirstRound() {
//        Game game = gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")));
//
//        game.getWhiteCardsDeck().remove(game.getWhiteCardsDeck().get(0));
//
//        game = gameService.startRound(game);
//
//        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round.xml")
//    void testStartRound_SecondRound() {
//        Game game = gameService.startRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
//
//        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
//    }
//
//    @Test
//    void testStartRound_GameNotStarted() {
//        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.startRound(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")))));
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round.xml")
//    void testEndRound_Rounds_NotEnding() {
//        Game game = gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
//
//        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round_endRound_RoundEnding.xml")
//    void testEndRound_Rounds_Ending() {
//        Game game = gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
//
//        Assertions.assertEquals(GameStatusEnum.ENDING, game.getStatus());
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/game_endRound_Points_NotEnding.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round.xml")
//    void testEndRound_Points_NotEnding() {
//        Game game = gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
//
//        Assertions.assertEquals(GameStatusEnum.STARTED, game.getStatus());
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/game_endRound_Points_NotEnding.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player_endRound_Points.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round.xml")
//    void testEndRound_Points_Ending() {
//        Game game = gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"))));
//
//        Assertions.assertEquals(GameStatusEnum.ENDING, game.getStatus());
//    }
//
//    @Test
//    void testEndRound_GameNotStarted() {
//        Assertions.assertThrows(GameNotStartedException.class, () -> gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000")))));
//    }
//
//    @Test
//    void testEndRound_RoundNoStarted() {
//        Assertions.assertThrows(RoundNotStartedException.class, () -> gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")))));
//    }
//
//    @Test
//    @DatabaseSetup("classpath:dbunit/service/setup/model/player2.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/card.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/deckcard.xml")
//    @DatabaseSetup("classpath:dbunit/service/setup/model/round_endRound_RoundNotEnding.xml")
//    void testEndRound_RoundNotEnding() {
//        Assertions.assertThrows(RoundNotEndingException.class, () -> gameService.endRound(gameService.getByRoom(roomService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333")))));
//    }
//
//    @Test
//    void testPlayCard() {
//        // ToDo: test add card
//    }

}
