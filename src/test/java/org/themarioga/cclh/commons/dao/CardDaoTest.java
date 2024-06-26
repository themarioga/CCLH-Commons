package org.themarioga.cclh.commons.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.BaseTest;
import org.themarioga.cclh.commons.dao.intf.CardDao;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
class CardDaoTest extends BaseTest {

    @Autowired
    private CardDao cardDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testCreateCard-expected.xml", table = "T_CARD", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createCard() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        Card card = new Card();
        card.setText("Test card");
        card.setType(CardTypeEnum.WHITE);
        card.setDictionary(dictionary);

        cardDao.create(card);

        Assertions.assertEquals(1L, card.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testUpdateCard-expected.xml", table = "T_CARD", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateCard() {
        Card card = cardDao.findOne(0L);
        card.setText("Test card");
        card.setType(CardTypeEnum.WHITE);

        cardDao.update(card);
        getCurrentSession().flush();

        Assertions.assertEquals(0L, card.getId());
    }

    @Test
    void deleteCard() {
        Card card = cardDao.findOne(0L);

        cardDao.delete(card);

        long total = cardDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findCard() {
        Card card = cardDao.findOne(0L);

        Assertions.assertEquals(0L, card.getId());
        Assertions.assertEquals("First", card.getText());
        Assertions.assertEquals(CardTypeEnum.BLACK, card.getType());
        Assertions.assertEquals(0, card.getDictionary().getId());
    }

    @Test
    void findAllCards() {
        List<Card> cards = cardDao.findAll();

        Assertions.assertEquals(1, cards.size());

        Assertions.assertEquals(0L, cards.get(0).getId());
        Assertions.assertEquals("First", cards.get(0).getText());
        Assertions.assertEquals(CardTypeEnum.BLACK, cards.get(0).getType());
        Assertions.assertEquals(0, cards.get(0).getDictionary().getId());
    }

    @Test
    void countAllCards() {
        long total = cardDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
    void testFindCardsByDictionaryIdAndType() {
        Dictionary dictionary = dictionaryDao.findOne(0L);

        List<Card> cards = cardDao.findCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK);
        int cardNumber = cardDao.countCardsByDictionaryIdAndType(dictionary, CardTypeEnum.BLACK);

        Assertions.assertEquals(1L, cards.size());
        Assertions.assertEquals(1L, cardNumber);
        Assertions.assertEquals(cardNumber, cards.size());
    }

}

