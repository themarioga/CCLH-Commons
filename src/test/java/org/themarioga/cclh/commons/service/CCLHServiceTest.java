package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.services.intf.GameService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
class CCLHServiceTest extends BaseTest {

    @Autowired
    GameService gameService;

    @Test
    void completeGameTest() {
        Game game = gameService.create(0L, "Habitación", 0L);
        gameService.setType(0L, GameTypeEnum.DEMOCRACY);
        gameService.setNumberOfCardsToWin(0L, 3);
        gameService.setDictionary(0L, 0L);
        gameService.addPlayer(0L, 0L);
        gameService.addPlayer(0L, 1L);
        gameService.addPlayer(0L, 3L);
        gameService.startGame(0L);
        gameService.startRound(0L);
        gameService.playCard(0L, 0L, game.getPlayers().get(0).getHand().get(0).getId());
        gameService.playCard(0L, 1L, game.getPlayers().get(1).getHand().get(0).getId());
        gameService.playCard(0L, 3L, game.getPlayers().get(2).getHand().get(0).getId());
        gameService.voteForCard(0L, 0L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(0L, 1L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(0L, 3L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.endRound(0L);
        gameService.startRound(0L);
        gameService.playCard(0L, 0L, game.getPlayers().get(0).getHand().get(0).getId());
        gameService.playCard(0L, 1L, game.getPlayers().get(1).getHand().get(0).getId());
        gameService.playCard(0L, 3L, game.getPlayers().get(2).getHand().get(0).getId());
        gameService.voteForCard(0L, 0L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(0L, 1L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(0L, 3L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.endRound(0L);
    }

}
