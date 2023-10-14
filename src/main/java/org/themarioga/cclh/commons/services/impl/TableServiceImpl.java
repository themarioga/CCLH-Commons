package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.TableDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;
import org.themarioga.cclh.commons.services.intf.TableService;
import org.themarioga.cclh.commons.util.Assert;

@Service
public class TableServiceImpl implements TableService {

    private final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    private final TableDao tableDao;

    @Autowired
    public TableServiceImpl(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    @Override
    public Table create(Game game) {
        logger.debug("Creating table for game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Create table
        Table table = new Table();
        table.setGameId(game.getId());
        table.setCurrentRoundNumber(0);

        // Set dictator if needed
        if (game.getType() == GameTypeEnum.CLASSIC || game.getType() == GameTypeEnum.DICTATORSHIP) {
            table.setCurrentPresident(game.getCreator());
        }

        return tableDao.create(table);
    }

    @Override
    public Table startRound(Game game) {
        logger.debug("Starting round for the game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Increment round number
        table.setCurrentRoundNumber(table.getCurrentRoundNumber() + 1);

        // Set current black card
        transferCardFromDeckToTable(game);

        // Set current president if needed
        if (game.getType() == GameTypeEnum.CLASSIC) {
            selectPlayerForRoundPresident(game);
        }

        return tableDao.update(table);
    }

    @Override
    public Table endRound(Game game) {
        logger.debug("Ending round for the game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Empty table
        table.getPlayerVotes().clear();
        table.getPlayedCards().clear();
        table.setCurrentBlackCard(null);

        return tableDao.update(table);
    }

    @Override
    public PlayedCard getMostVotedCard(Long gameId) {
        logger.debug("Getting most voted card of the table of the game {}", gameId);

        return tableDao.getMostVotedCard(gameId);
    }

    private void transferCardFromDeckToTable(Game game) {
        logger.debug("Transferring black card from deck to table in game {}", game);

        if (game.getDeck() != null && !game.getDeck().isEmpty()) {
            Card nextBlackCard = game.getDeck().get(0);
            game.getTable().setCurrentBlackCard(nextBlackCard);
            game.getDeck().remove(nextBlackCard);
        }
    }

    private void selectPlayerForRoundPresident(Game game) {
        int playerIndex = getCurrentPresidentIndex(game);

        if (playerIndex + 1 < game.getPlayers().size()) {
            game.getTable().setCurrentPresident(game.getPlayers().get(playerIndex + 1).getUser());
        } else {
            game.getTable().setCurrentPresident(game.getPlayers().get(0).getUser());
        }
    }

    private int getCurrentPresidentIndex(Game game) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getId().equals(game.getTable().getCurrentPresident().getId()))
                return i;
        }

        return 0;
    }

}
