package org.themarioga.engine.cah.dao.dictionaries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.dao.intf.dictionaries.DictionaryDao;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.dictionaries.DictionaryCollaborator;
import org.themarioga.engine.commons.dao.intf.LanguageDao;
import org.themarioga.engine.commons.dao.intf.UserDao;
import org.themarioga.engine.commons.models.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/dictionary.xml")
class DictionaryDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DictionaryDao dictionaryDao;
    @Autowired
    private LanguageDao languageDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionary-expected.xml", table = "dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createDictionary() {
        User user = userDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Dictionary dictionary = new Dictionary();
        dictionary.setName("Test deck");
        dictionary.setShared(true);
        dictionary.setPublished(true);
        dictionary.setCreator(user);
        dictionary.setLang(languageDao.getLanguage("es"));
        dictionary.setCreationDate(new Date());

        dictionary = dictionaryDao.createOrUpdate(dictionary);
        getCurrentSession().flush();

        Assertions.assertNotNull(dictionary.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionary-expected.xml", table = "dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        dictionary.setName("Otro nombre");
        dictionary.setShared(false);
        dictionary.setPublished(false);

        dictionaryDao.createOrUpdate(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), dictionary.getId());
    }

    @Test
    void deleteDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        dictionaryDao.delete(dictionary);

        long total = dictionaryDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), dictionary.getId());
        Assertions.assertEquals("First", dictionary.getName());
        Assertions.assertEquals(true, dictionary.getShared());
        Assertions.assertEquals(true, dictionary.getPublished());
    }

    @Test
    void findAllDictionarys() {
        List<Dictionary> dictionaries = dictionaryDao.findAll();

        Assertions.assertEquals(1, dictionaries.size());

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), dictionaries.get(0).getId());
        Assertions.assertEquals("First", dictionaries.get(0).getName());
        Assertions.assertEquals(true, dictionaries.get(0).getShared());
        Assertions.assertEquals(true, dictionaries.get(0).getPublished());
    }

    @Test
    void countAllDictionarys() {
        long total = dictionaryDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionaryCollaborators-expected.xml", table = "dictionary_collaborator", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addDictionaryCollaborator() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        User user = userDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        DictionaryCollaborator dictionaryCollaborator = new DictionaryCollaborator();
        dictionaryCollaborator.setDictionary(dictionary);
        dictionaryCollaborator.setUser(user);
        dictionaryCollaborator.setAccepted(true);
        dictionaryCollaborator.setCanEdit(true);
        dictionary.getCollaborators().add(dictionaryCollaborator);

        dictionaryDao.createOrUpdate(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1, dictionary.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/dictionarycollaborators.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionaryCollaborators-expected.xml", table = "dictionary_collaborator", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionaryCollaborator() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        dictionary.getCollaborators().get(0).setAccepted(false);

        dictionaryDao.createOrUpdate(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1, dictionary.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/dictionarycollaborators.xml")
    void getDictionaryCollaborators() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(true, dictionary.getCollaborators().get(0).getAccepted());
    }

}
