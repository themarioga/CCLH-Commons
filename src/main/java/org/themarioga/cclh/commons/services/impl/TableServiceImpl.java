package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.TableDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.DictionaryService;
import org.themarioga.cclh.commons.services.intf.GameService;
import org.themarioga.cclh.commons.services.intf.PlayerService;
import org.themarioga.cclh.commons.services.intf.TableService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TableServiceImpl implements TableService {

    private final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    @Autowired
    TableDao tableDao;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    DictionaryService dictionaryService;

    @Override
    public Table create(Table table) {
        logger.debug("Creating table: {}", table);

        return tableDao.create(table);
    }

    @Override
    public Table update(Table table) {
        logger.debug("Updating table: {}", table);

        return tableDao.update(table);
    }

    @Override
    public void delete(Table table) {
        logger.debug("Delete table: {}", table);

        tableDao.delete(table);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete table by ID: {}", id);

        tableDao.deleteById(id);
    }

    @Override
    public Table findOne(long id) {
        logger.debug("Getting table with ID: {}", id);

        return tableDao.findOne(id);
    }

    @Override
    public void addBlackCardsToTableDeck(Game game) {
        logger.debug("Adding black cards to table in the game {}", game);

        List<Card> cards = new ArrayList<>(dictionaryService.findCardsByDictionaryIdAndType(game.getDictionary(), CardTypeEnum.BLACK));

        Collections.shuffle(cards);

        game.getTable().getDeck().addAll(cards);

        gameService.update(game);
    }

    @Override
    public void transferCardFromDeckToTable(Table table) {
        logger.debug("Transferring black card from deck to table {}", table);

        if (!table.getDeck().isEmpty()) {
            Card nextBlackCard = table.getDeck().get(0);
            table.setCurrentBlackCard(nextBlackCard);
            table.getDeck().remove(nextBlackCard);

            tableDao.update(table);
        }
    }

    @Override
    public void addWhiteCardsToPlayersDecks(Game game) {
        logger.debug("Adding white cards to players in the game {}", game);

        List<Card> cards = new ArrayList<>(dictionaryService.findCardsByDictionaryIdAndType(game.getDictionary(), CardTypeEnum.WHITE));

        Collections.shuffle(cards);

        int cardsPerPlayer = Math.floorDiv(cards.size(), game.getNumberOfPlayers());

        for (Player player : game.getPlayers()) {
            List<Card> playerCards = cards.subList(0, cardsPerPlayer);

            player.getDeck().addAll(playerCards);
            playerService.update(player);

            cards.removeAll(playerCards);
        }
    }

    @Override
    public PlayedCard getMostVotedCard(Long gameId) {
        logger.debug("Getting most voted card of the table {}", gameId);

        return tableDao.getMostVotedCard(gameId);
    }

}
