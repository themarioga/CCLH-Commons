package org.themarioga.engine.cah.service.dictionaries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.exceptions.dictionary.*;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.dictionaries.DictionaryCollaborator;
import org.themarioga.engine.cah.services.intf.dictionaries.DictionaryService;
import org.themarioga.engine.commons.models.User;
import org.themarioga.engine.commons.services.intf.LanguageService;
import org.themarioga.engine.commons.services.intf.UserService;

import java.util.List;
import java.util.UUID;

@DatabaseSetup("classpath:dbunit/service/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/service/setup/user.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionary.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/card.xml")
@DatabaseSetup("classpath:dbunit/service/setup/dictionaries/dictionarycollaborators.xml")
class DictionaryServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private LanguageService languageService;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/dictionaries/testCreateDictionary-expected.xml", table = "Dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/dictionaries/testDeleteDictionary-expected.xml", table = "Dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testDelete() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222"));

        dictionaryService.delete(dictionary);
        getCurrentSession().flush();

        Assertions.assertNull(dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222")));
    }

    @Test
    void testDelete_AlreadyShared() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryAlreadySharedException.class, () -> dictionaryService.delete(dictionary));
    }

    @Test
    void testGetDictionaryByUUID() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertNotNull(dictionary);
        Assertions.assertEquals("First", dictionary.getName());
    }

    @Test
    void testGetDictionariesByCreator() {
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        List<Dictionary> dictionaryList = dictionaryService.getDictionariesByCreator(user);

        Assertions.assertEquals(3, dictionaryList.size());
    }

    @Test
    void testGetDictionariesPaginatedForTable() {
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        List<Dictionary> dictionaryList = dictionaryService.getDictionariesPaginatedForTable(user, 0, 1);

        Assertions.assertEquals(1, dictionaryList.size());
    }

    @Test
    void testCountDictionariesPaginatedForTable() {
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        Long nDic = dictionaryService.getDictionaryCountForTable(user);

        Assertions.assertEquals(1, nDic);
    }

    // //////// COLLABORATORS //////////

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/dictionaries/testCreateCollaborator-expected.xml", table = "dictionary_collaborator", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testAddCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        DictionaryCollaborator dictionaryCollaborator = dictionaryService.addCollaborator(dictionary, user);
        getCurrentSession().flush();
        Assertions.assertNotNull(dictionaryCollaborator);
    }

    @Test
    void testAddCollaborator_MaxCollaboratorsReached() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(DictionaryMaxCollaboratorsReached.class, () -> dictionaryService.addCollaborator(dictionary, user));
    }

    @Test
    void testAddCollaborator_CollaboratorAlreadyExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryCollaboratorAlreadyExists.class, () -> dictionaryService.addCollaborator(dictionary, user));
    }

    @Test
    void testToggleAcceptedCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        DictionaryCollaborator dictionaryCollaborator = dictionaryService.toggleAcceptedCollaborator(dictionary, user);

        Assertions.assertEquals(false, dictionaryCollaborator.getAccepted());
    }

    @Test
    void testToggleAcceptedCollaborator_CreatorCantBeAltered() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryCollaboratorCreatorCantBeAltered.class, () -> dictionaryService.toggleAcceptedCollaborator(dictionary, user));
    }

    @Test
    void testToggleAcceptedCollaborator_CollaboratorDoesntExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(DictionaryCollaboratorDoesntExists.class, () -> dictionaryService.toggleAcceptedCollaborator(dictionary, user));
    }

    @Test
    void testToggleCanEditCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        DictionaryCollaborator dictionaryCollaborator = dictionaryService.toggleCanEditCollaborator(dictionary, user);

        Assertions.assertEquals(false, dictionaryCollaborator.getCanEdit());
    }

    @Test
    void testToggleCanEditCollaborator_CreatorCantBeAltered() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryCollaboratorCreatorCantBeAltered.class, () -> dictionaryService.toggleCanEditCollaborator(dictionary, user));
    }

    @Test
    void testToggleCanEditCollaborator_CollaboratorDoesntExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(DictionaryCollaboratorDoesntExists.class, () -> dictionaryService.toggleCanEditCollaborator(dictionary, user));
    }

    @Test
    void testToggleCanEditCollaborator_CollaboratorNotAccepted() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertThrows(DictionaryCollaboratorDoesntExists.class, () -> dictionaryService.toggleCanEditCollaborator(dictionary, user));
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/service/expected/dictionaries/testDeleteCollaborator-expected.xml", table = "dictionary_collaborator", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testRemoveCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        dictionaryService.removeCollaborator(dictionary, user);
        getCurrentSession().flush();

        Assertions.assertEquals(1, dictionary.getCollaborators().size());
    }

    @Test
    void testRemoveCollaborator_CreatorCantBeAltered() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertThrows(DictionaryCollaboratorCreatorCantBeAltered.class, () -> dictionaryService.removeCollaborator(dictionary, user));
    }

    @Test
    void testRemoveCollaborator_CollaboratorDoesntExists() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertThrows(DictionaryCollaboratorDoesntExists.class, () -> dictionaryService.removeCollaborator(dictionary, user));
    }

    @Test
    void testIsCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertTrue(dictionaryService.isDictionaryCollaborator(dictionary, user));
    }

    @Test
    void testIsCollaborator_IsNotCollaborator() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        Assertions.assertFalse(dictionaryService.isDictionaryCollaborator(dictionary, user));
    }

    @Test
    void testIsEditor() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userService.getById(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertTrue(dictionaryService.isDictionaryEditor(dictionary, user));
    }

    @Test
    void testIsEditor_IsNotEditor() {
        Dictionary dictionary = dictionaryService.getDictionaryById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertFalse(dictionaryService.isDictionaryEditor(dictionary, user));
    }

    @Test
    void testGetDictionariesByCollaborator() {
        User user = userService.getById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Assertions.assertEquals(1, dictionaryService.getDictionariesByCollaborator(user).size());
    }

}
