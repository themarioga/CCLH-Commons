package org.themarioga.game.cah.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.exceptions.dictionary.*;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.services.intf.DictionaryService;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.LanguageService;
import org.themarioga.game.commons.services.intf.UserService;

import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/card.xml")
class DictionaryServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private LanguageService languageService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/testCreateDictionary-expected.xml", table = "Dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testCreateDictionary() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Dictionary dictionary = dictionaryService.create("Dictionary 1", creator);
        getCurrentSession().flush();

        Assertions.assertNotNull(dictionary);
    }

    @Test
    void testCreateDictionary_NameAlreadyExists() {
        User creator = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryAlreadyExistsException.class, () -> dictionaryService.create("First", creator));
    }

    @Test
    void testCreateDictionary_TooManyDictionaries() {
        User creator = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(DictionaryAlreadyFilledException.class, () -> dictionaryService.create("Dictionary 1", creator));
    }

    @Test
    void testSetName() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        dictionary = dictionaryService.setName(dictionary, "New Name");

        Assertions.assertEquals("New Name", dictionary.getName());
    }

    @Test
    void testSetName_NameAlreadyExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryAlreadyExistsException.class, () -> dictionaryService.setName(dictionary, "First"));
    }

    @Test
    void testSetLanguage() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        dictionary = dictionaryService.setLanguage(dictionary, languageService.getLanguage("en"));

        Assertions.assertEquals("en", dictionary.getLang().getId());
    }

    @Test
    void testTogglePublished() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        dictionary = dictionaryService.togglePublished(dictionary);

        Assertions.assertEquals(false, dictionary.getPublished());
    }

    @Test
    void testTogglePublished_AlreadyShared() {
	    Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

	    Assertions.assertThrows(DictionaryAlreadySharedException.class, () -> dictionaryService.togglePublished(dictionary));
    }

    @Test
    void testTogglePublished_DictionaryNotCompleted() {
	    Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222"));

	    Assertions.assertThrows(DictionaryNotCompletedException.class, () -> dictionaryService.togglePublished(dictionary));
    }

	@Test
	void testToggleShared() {
		Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
		dictionary = dictionaryService.toggleShared(dictionary);

		Assertions.assertEquals(true, dictionary.getShared());
	}

	@Test
	void testToggleShared_NotPublished() {
		Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222"));

		Assertions.assertThrows(DictionaryNotPublishedException.class, () -> dictionaryService.toggleShared(dictionary));
	}

}
