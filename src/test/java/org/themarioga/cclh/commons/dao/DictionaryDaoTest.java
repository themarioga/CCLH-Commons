package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.DictionaryCollaborator;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
class DictionaryDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionary-expected.xml", table = "T_DICTIONARY", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createDictionary() {
        User user = userDao.findOne(0L);

        Dictionary dictionary = new Dictionary();
        dictionary.setName("Test deck");
        dictionary.setShared(true);
        dictionary.setPublished(true);
        dictionary.setCreator(user);

        dictionaryDao.create(dictionary);
        getCurrentSession().flush();

        Assertions.assertEquals(1L, dictionary.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionary-expected.xml", table = "T_DICTIONARY", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    void getDictionaryCards() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Assertions.assertEquals(1, dictionary.getCards().size());
    }

}
