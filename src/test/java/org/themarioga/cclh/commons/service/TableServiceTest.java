package org.themarioga.cclh.commons.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.services.intf.TableService;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/room.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/game.xml")
@DatabaseSetup("classpath:dbunit/service/setup/player.xml")
@DatabaseSetup("classpath:dbunit/service/setup/table.xml")
class TableServiceTest extends BaseTest {

    @Autowired
    TableService tableService;

    @Test
    @DatabaseSetup("classpath:dbunit/service/setup/tableplayedcards.xml")
    @DatabaseSetup("classpath:dbunit/service/setup/tableplayervotes.xml")
    void testGetMostVotedCard() {
        PlayedCard mostVotedCard = tableService.getMostVotedCard(10L);

        Assertions.assertNotNull(mostVotedCard);
    }

}
