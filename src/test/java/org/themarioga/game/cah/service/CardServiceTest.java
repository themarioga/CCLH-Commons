package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.exceptions.card.CardAlreadyExistsException;
import org.themarioga.game.cah.exceptions.card.CardTextExcededLength;
import org.themarioga.game.cah.exceptions.dictionary.DictionaryAlreadyFilledException;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.cah.services.intf.CardService;
import org.themarioga.game.cah.services.intf.DictionaryService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionarycollaborators.xml")
class CardServiceTest extends BaseTest {

    @Autowired
    CardService cardService;

    @Autowired
    DictionaryService dictionaryService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateCard-expected.xml", table = "Card", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateCard() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Card card = cardService.create(dictionary, CardTypeEnum.WHITE, "Test card");
        getCurrentSession().flush();
        Assertions.assertNotNull(card);
    }

    @Test
    void testCreateCard_AlreadyExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(CardAlreadyExistsException.class, () -> cardService.create(dictionary, CardTypeEnum.WHITE, "Another white card"));
    }

    @Test
    void testCreateCard_AlreadyFilled() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryAlreadyFilledException.class, () -> cardService.create(dictionary, CardTypeEnum.WHITE, "Test card"));
    }

    @Test
    void testCreateCard_TextLengthExceeded() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(CardTextExcededLength.class, () -> cardService.create(dictionary, CardTypeEnum.WHITE, "This test card have a very long text"));
    }

}
