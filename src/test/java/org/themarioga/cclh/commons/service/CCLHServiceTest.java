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
        gameService.setType(game, GameTypeEnum.DEMOCRACY);
        gameService.setNumberOfCardsToWin(game, 3);
        gameService.setDictionary(game, 0L);
        gameService.addPlayer(game, 0L);
        gameService.addPlayer(game, 1L);
        gameService.addPlayer(game, 3L);
        gameService.startGame(game);
        gameService.startRound(game);
        gameService.playCard(game, 0L, game.getPlayers().get(0).getHand().get(0).getId());
        gameService.playCard(game, 1L, game.getPlayers().get(1).getHand().get(0).getId());
        gameService.playCard(game, 3L, game.getPlayers().get(2).getHand().get(0).getId());
        gameService.voteForCard(game, 0L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(game, 1L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(game, 3L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.endRound(game);
        gameService.startRound(game);
        gameService.playCard(game, 0L, game.getPlayers().get(0).getHand().get(0).getId());
        gameService.playCard(game, 1L, game.getPlayers().get(1).getHand().get(0).getId());
        gameService.playCard(game, 3L, game.getPlayers().get(2).getHand().get(0).getId());
        gameService.voteForCard(game, 0L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(game, 1L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.voteForCard(game, 3L, game.getTable().getPlayedCards().get(0).getCard().getId());
        gameService.endRound(game);
    }

}
