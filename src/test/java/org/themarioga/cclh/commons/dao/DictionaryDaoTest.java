package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
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
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionary-expected.xml", table = "T_DICTIONARY")
    void createDictionary() {
        User user = userDao.findOne(0);

        Dictionary dictionary = new Dictionary();
        dictionary.setName("Test dictionary");
        dictionary.setShared(true);
        dictionary.setPublished(true);
        dictionary.setCreator(user);

        dictionaryDao.create(dictionary);
        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionary-expected.xml", table = "T_DICTIONARY")
    void updateDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(0L);
        dictionary.setName("Otro nombre");
        dictionary.setShared(false);
        dictionary.setPublished(false);

        dictionaryDao.update(dictionary);
        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    void deleteDictionary() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        dictionaryDao.delete(dictionary);
        dictionaryDao.getCurrentSession().flush();

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
        List<Dictionary> dictionarys = dictionaryDao.findAll();

        Assertions.assertEquals(1, dictionarys.size());

        Assertions.assertEquals(0L, dictionarys.get(0).getId());
        Assertions.assertEquals("First", dictionarys.get(0).getName());
        Assertions.assertEquals(true, dictionarys.get(0).getShared());
        Assertions.assertEquals(true, dictionarys.get(0).getPublished());
    }

    @Test
    void countAllDictionarys() {
        long total = dictionaryDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS")
    void addDictionaryCollaborator() {
        Dictionary dictionary = dictionaryDao.findOne(0L);
        User user = userDao.findOne(0L);

        DictionaryCollaborator dictionaryCollaborator = new DictionaryCollaborator();
        dictionaryCollaborator.setDictionaryId(0L);
        dictionaryCollaborator.setUser(user);
        dictionaryCollaborator.setAccepted(true);
        dictionaryCollaborator.setCanEdit(true);
        dictionary.getDictionaryCollaborators().add(dictionaryCollaborator);

        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS")
    void updateDictionaryCollaborator() {
        Dictionary dictionary = dictionaryDao.findOne(0L);
        dictionary.getDictionaryCollaborators().get(0).setAccepted(false);

        dictionaryDao.getCurrentSession().flush();
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    void getDictionaryCollaborators() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Assertions.assertEquals(true, dictionary.getDictionaryCollaborators().get(0).getAccepted());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    void getDictionaryCards() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Assertions.assertEquals(1, dictionary.getCards().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    void testFindCardsByDictionaryIdAndType() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        List<Card> cards = dictionaryDao.findCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK);
        int cardNumber = dictionaryDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK);

        Assertions.assertEquals(1L, cards.size());
        Assertions.assertEquals(1L, cardNumber);
        Assertions.assertEquals(cardNumber, cards.size());
    }

}
