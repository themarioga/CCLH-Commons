package org.themarioga.cclh.daos;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.BaseTest;
import org.themarioga.cclh.daos.intf.CardDao;
import org.themarioga.cclh.daos.intf.DictionaryDao;
import org.themarioga.cclh.enums.CardTypeEnum;
import org.themarioga.cclh.models.Card;
import org.themarioga.cclh.models.Dictionary;

import java.util.List;

@DatabaseSetup("classpath:dbunit/dao/setup/user.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/dictionary.xml")
@DatabaseSetup("classpath:dbunit/dao/setup/card.xml")
public class CardDaoTest extends BaseTest {

    @Autowired
    private CardDao cardDao;
    @Autowired
    private DictionaryDao dictionaryDao;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testCreateCard-expected.xml", table = "T_CARD")
    public void createCard() {
        Dictionary dictionary = dictionaryDao.findOne(0);

        Card card = new Card();
        card.setText("Test card");
        card.setType(CardTypeEnum.BLACK);
        card.setDictionary(dictionary);

        cardDao.create(card);
        cardDao.getCurrentSession().flush();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/dao/expected/card/testUpdateCard-expected.xml", table = "T_CARD")
    public void updateCard() {
        Card card = cardDao.findOne(0L);
        card.setText("Test card");
        card.setType(CardTypeEnum.BLACK);

        cardDao.update(card);
        cardDao.getCurrentSession().flush();
    }

    @Test
    public void deleteCard() {
        Card card = cardDao.findOne(0L);

        cardDao.delete(card);
        cardDao.getCurrentSession().flush();

        long total = cardDao.countAll();

        Assertions.assertEquals(0, total);
    }

    @Test
    public void findCard() {
        Card card = cardDao.findOne(0L);

        Assertions.assertEquals(0L, card.getId());
        Assertions.assertEquals("First", card.getText());
        Assertions.assertEquals(CardTypeEnum.WHITE, card.getType());
        Assertions.assertEquals(0, card.getDictionary().getId());
    }

    @Test
    public void findAllCards() {
        List<Card> cards = cardDao.findAll();

        Assertions.assertEquals(cards.size(), 1);

        Assertions.assertEquals(0L, cards.get(0).getId());
        Assertions.assertEquals("First", cards.get(0).getText());
        Assertions.assertEquals(CardTypeEnum.WHITE, cards.get(0).getType());
        Assertions.assertEquals(0, cards.get(0).getDictionary().getId());
    }

    @Test
    public void countAllCards() {
        long total = cardDao.countAll();

        Assertions.assertEquals(1, total);
    }

}

