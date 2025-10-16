package org.themarioga.engine.cah.dao.dictionaries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.engine.cah.BaseTest;
import org.themarioga.engine.cah.dao.intf.dictionaries.CardDao;
import org.themarioga.engine.cah.dao.intf.dictionaries.DictionaryDao;
import org.themarioga.engine.cah.enums.CardTypeEnum;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DatabaseSetup("classpath:dbunit/dao/setup/lang.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/card.xml")
class CardDaoTest extends BaseTest {

    @Autowired
    private CardDao cardDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testCreateCard-expected.xml", table = "Card", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createCard() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Card card = new Card();
        card.setText("Test card");
        card.setType(CardTypeEnum.WHITE);
        card.setDictionary(dictionary);
        card.setCreationDate(new Date());

        card = cardDao.createOrUpdate(card);
        getCurrentSession().flush();

        Assertions.assertNotNull(card.getId());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testUpdateCard-expected.xml", table = "Card", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void updateCard() {
        Card card = cardDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        card.setText("Test card");
        card.setType(CardTypeEnum.WHITE);

        cardDao.createOrUpdate(card);
        getCurrentSession().flush();

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), card.getId());
    }

    @Test
    void deleteCard() {
        Card card = cardDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        cardDao.delete(card);

        long total = cardDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    void findCard() {
        Card card = cardDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), card.getId());
        Assertions.assertEquals("First", card.getText());
        Assertions.assertEquals(CardTypeEnum.BLACK, card.getType());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), card.getDictionary().getId());
    }

    @Test
    void findAllCards() {
        List<Card> cards = cardDao.findAll();

        Assertions.assertEquals(1, cards.size());

        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), cards.get(0).getId());
        Assertions.assertEquals("First", cards.get(0).getText());
        Assertions.assertEquals(CardTypeEnum.BLACK, cards.get(0).getType());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), cards.get(0).getDictionary().getId());
    }

    @Test
    void countAllCards() {
        long total = cardDao.countAll();

        Assertions.assertEquals(1, total);
    }

    @Test
    @DatabaseSetup("classpath:dbunit/dao/setup/dictionaries/card.xml")
    void testFindCardsByDictionaryAndType() {
        Dictionary dictionary = dictionaryDao.findOne(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        List<Card> cards = cardDao.findCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK);
        int cardNumber = cardDao.countCardsByDictionaryAndType(dictionary, CardTypeEnum.BLACK);

        Assertions.assertEquals(1L, cards.size());
        Assertions.assertEquals(1L, cardNumber);
        Assertions.assertEquals(cardNumber, cards.size());
    }

}

