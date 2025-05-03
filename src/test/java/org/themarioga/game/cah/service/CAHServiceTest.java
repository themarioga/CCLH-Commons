package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.commons.services.intf.PlayerService;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
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

}
