package org.themarioga.game.cah.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.game.cah.BaseTest;
import org.themarioga.game.cah.dao.intf.DictionaryDao;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.DictionaryCollaborator;
import org.themarioga.game.commons.dao.intf.LanguageDao;
import org.themarioga.game.commons.dao.intf.UserDao;
import org.themarioga.game.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
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
        User user = userDao.findOne(0L);

        Dictionary dictionary = new Dictionary();
        dictionary.setName("Test deck");
        dictionary.setShared(true);
        dictionary.setPublished(true);
        dictionary.setCreator(user);
        dictionary.setLang(languageDao.getLanguage("es"));

        dictionaryDao.create(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1L, dictionary.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionary-expected.xml", table = "dictionary", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(0L);
        dictionary.setName("Otro nombre");
        dictionary.setShared(false);
        dictionary.setPublished(false);

        dictionaryDao.update(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, dictionary.getId());
    }

    @Test
    void deleteDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        dictionaryDao.delete(dictionary);

        long total = dictionaryDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Assertions.assertEquals(0L, dictionary.getId());
        Assertions.assertEquals("First", dictionary.getName());
        Assertions.assertEquals(true, dictionary.getShared());
        Assertions.assertEquals(true, dictionary.getPublished());
    }

    @Test
    void findAllDictionarys() {
        List<Dictionary> dictionaries = dictionaryDao.findAll();

        Assertions.assertEquals(1, dictionaries.size());

        Assertions.assertEquals(0L, dictionaries.get(0).getId());
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
        Dictionary dictionary = dictionaryDao.findOne(0L);
        User user = userDao.findOne(0L);

        DictionaryCollaborator dictionaryCollaborator = new DictionaryCollaborator();
        dictionaryCollaborator.setDictionary(dictionary);
        dictionaryCollaborator.setUser(user);
        dictionaryCollaborator.setAccepted(true);
        dictionaryCollaborator.setCanEdit(true);
        dictionary.getCollaborators().add(dictionaryCollaborator);

        dictionaryDao.update(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1, dictionary.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionaryCollaborators-expected.xml", table = "dictionary_collaborator", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionaryCollaborator() {
        Dictionary dictionary = dictionaryDao.findOne(0L);
        dictionary.getCollaborators().get(0).setAccepted(false);

        dictionaryDao.update(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1, dictionary.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    void getDictionaryCollaborators() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Assertions.assertEquals(true, dictionary.getCollaborators().get(0).getAccepted());
    }

}
