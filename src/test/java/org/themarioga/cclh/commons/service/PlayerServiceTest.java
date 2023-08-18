package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.services.intf.PlayerService;

@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
class PlayerServiceTest extends BaseTest {

    @Autowired
    PlayerService playerService;

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/playerdeck.xml")
    void testTransferCardsFromPlayerDeckToPlayerHand() {
        Player player = playerService.findOne(0L);

        Assertions.assertEquals(3, player.getDeck().size());
        Assertions.assertEquals("First white card", player.getDeck().get(0).getText());
        Assertions.assertEquals(0, player.getHand().size());

        playerService.transferCardsFromPlayerDeckToPlayerHand(player);

        Assertions.assertEquals(0, player.getDeck().size());
        Assertions.assertEquals(3, player.getHand().size());
        Assertions.assertEquals("First white card", player.getHand().get(0).getText());
    }

}
