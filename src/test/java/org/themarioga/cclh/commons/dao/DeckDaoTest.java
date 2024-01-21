package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.DeckDao;
import org.themarioga.cclh.commons.dao.intf.UserDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.models.DeckCollaborator;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
class DeckDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DeckDao deckDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionary-expected.xml", table = "T_DICTIONARY", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createDictionary() {
        User user = userDao.findOne(0L);

        Deck deck = new Deck();
        deck.setName("Test deck");
        deck.setShared(true);
        deck.setPublished(true);
        deck.setCreator(user);

        deckDao.create(deck);
        getCurrentSession().flush();

        Assertions.assertEquals(1L, deck.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionary-expected.xml", table = "T_DICTIONARY", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionary() {
        Deck deck = deckDao.findOne(0L);
        deck.setName("Otro nombre");
        deck.setShared(false);
        deck.setPublished(false);

        deckDao.update(deck);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, deck.getId());
    }

    @Test
    void deleteDictionary() {
        Deck deck = deckDao.findOne(0L);

        deckDao.delete(deck);

        long total = deckDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findDictionary() {
        Deck deck = deckDao.findOne(0L);

        Assertions.assertEquals(0L, deck.getId());
        Assertions.assertEquals("First", deck.getName());
        Assertions.assertEquals(true, deck.getShared());
        Assertions.assertEquals(true, deck.getPublished());
    }

    @Test
    void findAllDictionarys() {
        List<Deck> decks = deckDao.findAll();

        Assertions.assertEquals(1, decks.size());

        Assertions.assertEquals(0L, decks.get(0).getId());
        Assertions.assertEquals("First", decks.get(0).getName());
        Assertions.assertEquals(true, decks.get(0).getShared());
        Assertions.assertEquals(true, decks.get(0).getPublished());
    }

    @Test
    void countAllDictionarys() {
        long total = deckDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testCreateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void addDictionaryCollaborator() {
        Deck deck = deckDao.findOne(0L);
        User user = userDao.findOne(0L);

        DeckCollaborator deckCollaborator = new DeckCollaborator();
        deckCollaborator.setDictionaryId(0L);
        deckCollaborator.setUser(user);
        deckCollaborator.setAccepted(true);
        deckCollaborator.setCanEdit(true);
        deck.getCollaborators().add(deckCollaborator);

        deckDao.update(deck);
        getCurrentSession().flush();

        Assertions.assertEquals(1, deck.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/dictionary/testUpdateDictionaryCollaborators-expected.xml", table = "T_DICTIONARY_COLLABORATORS", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateDictionaryCollaborator() {
        Deck deck = deckDao.findOne(0L);
        deck.getCollaborators().get(0).setAccepted(false);

        deckDao.update(deck);
        getCurrentSession().flush();

        Assertions.assertEquals(1, deck.getCollaborators().size());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionarycollaborators.xml")
    void getDictionaryCollaborators() {
        Deck deck = deckDao.findOne(0L);

        Assertions.assertEquals(true, deck.getCollaborators().get(0).getAccepted());
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    void getDictionaryCards() {
        Deck deck = deckDao.findOne(0L);

        Assertions.assertEquals(1, deck.getCards().size());
    }

}
