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

import java.util.List;
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

	@Test
	void testChangeText() {
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		card = cardService.changeText(card, "New text");

		Assertions.assertEquals("New text", card.getText());
	}

	@Test
	void testChangeText_CardAlreadyExists() {
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Assertions.assertThrows(CardAlreadyExistsException.class, () -> cardService.changeText(card, "Second black card"));
	}

	@Test
	void testChangeText_TextLengthExceeded() {
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Assertions.assertThrows(CardTextExcededLength.class, () -> cardService.changeText(card, "This test card will have a very long text"));
	}

	@Test
	@ExpectedDatabase(value = "classpath:dbunit/service/expected/testDeleteCard-expected.xml", table = "Card", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
	void testDelete() {
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		cardService.delete(card);
		getCurrentSession().flush();

		Assertions.assertNull(cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000")));
	}

	@Test
	void testGetCardById(){
		Card card = cardService.getCardById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Assertions.assertNotNull(card);
		Assertions.assertEquals("First black card", card.getText());
	}

	@Test
	void testFindCardsByDictionaryAndType() {
		Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		List<Card> blackCards = cardService.findCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK);
		Assertions.assertEquals(3, blackCards.size());

		List<Card> whiteCards = cardService.findCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE);
		Assertions.assertEquals(15, whiteCards.size());
	}

	@Test
	void testCountCardsByDictionaryAndType() {
		Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Assertions.assertEquals(3, cardService.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK));

		Assertions.assertEquals(15, cardService.countCardsByDictionaryAndType(dictionary, CardTypeEnum.WHITE));
	}

	@Test
	void testCheckDictionaryCanBePublished() {
		Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		Assertions.assertTrue(cardService.checkDictionaryCanBePublished(dictionary));
	}

}
