package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.TableDao;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.enums.TableStatusEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.card.CardAlreadyPlayedException;
import org.themarioga.cclh.commons.exceptions.card.CardNotPlayedException;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyPlayedCardException;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyVotedCardException;
import org.themarioga.cclh.commons.exceptions.player.PlayerCannotVoteCardException;
import org.themarioga.cclh.commons.exceptions.table.TableWrongStatusException;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.PlayerService;
import org.themarioga.cclh.commons.services.intf.TableService;
import org.themarioga.cclh.commons.util.Assert;

@Service
public class TableServiceImpl implements TableService {

    private final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    private final TableDao tableDao;
    private final PlayerService playerService;

    @Autowired
    public TableServiceImpl(TableDao tableDao, PlayerService playerService) {
        this.tableDao = tableDao;
        this.playerService = playerService;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Table create(Game game) {
        logger.debug("Creating table for game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Create table
        Table table = new Table();
        table.setGame(game);
        table.setStatus(TableStatusEnum.STARTING);
        table.setCurrentRoundNumber(0);

        // Set dictator if needed
        if (game.getType() == GameTypeEnum.CLASSIC || game.getType() == GameTypeEnum.DICTATORSHIP) {
            table.setCurrentPresident(playerService.findByUser(game.getCreator()));
        }

        return tableDao.create(table);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Table startRound(Game game) {
        logger.debug("Starting round for the game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Check if the table is ready to start
        if (table.getStatus() != TableStatusEnum.STARTING)
            throw new TableWrongStatusException();

        // Set the table mode to play
        table.setStatus(TableStatusEnum.PLAYING);

        // Increment round number
        table.setCurrentRoundNumber(table.getCurrentRoundNumber() + 1);

        // Set current president if needed
        if (game.getType() == GameTypeEnum.CLASSIC) {
            selectPlayerForRoundPresident(game);
        }

        return tableDao.update(table);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Table endRound(Game game) {
        logger.debug("Ending round for the game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Check if the table is ready to start
        if (table.getStatus() != TableStatusEnum.ENDING)
            throw new TableWrongStatusException();

        // Set the table mode to play
        table.setStatus(TableStatusEnum.STARTING);

        // Empty table
        table.getVotedCards().clear();
        table.getPlayedCards().clear();
        table.setCurrentBlackCard(null);

        return tableDao.update(table);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Table playCard(Game game, Player player, Card card) {
        logger.debug("Player {} playing card {} for the game {}", player, card, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Check if the table is ready to start
        if (table.getStatus() != TableStatusEnum.PLAYING)
            throw new TableWrongStatusException();

        // Check if the player already played
        if (table.getPlayedCards().stream().anyMatch(playedCard -> playedCard.getPlayer().getId().equals(player.getId())))
            throw new PlayerAlreadyPlayedCardException();

        // Check if the card was already played
        if (table.getPlayedCards().stream().anyMatch(playedCard -> playedCard.getCard().getId().equals(card.getId())))
            throw new CardAlreadyPlayedException();

        // Remove the card from the player hand
        playerService.removeCardFromHand(player, card);

        // Set the played card
        PlayedCard playedCard = new PlayedCard();
        playedCard.setTable(table);
        playedCard.setPlayer(player);
        playedCard.setCard(card);
        table.getPlayedCards().add(playedCard);

        // Check we need to proceed to voting
        if (checkIfEveryoneHavePlayedACard(game, table)) {
            table.setStatus(TableStatusEnum.VOTING);


        }

        return tableDao.update(table);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Table voteCard(Game game, Player player, Card card) {
        logger.debug("Player {} voting card {} for the game {}", player, card, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check table exists
        Table table = game.getTable();
        Assert.assertNotNull(table, ErrorEnum.GAME_NOT_FOUND);

        // Check if the table is ready to start
        if (table.getStatus() != TableStatusEnum.VOTING)
            throw new TableWrongStatusException();

        // Check if the player can vote
        if ((game.getType().equals(GameTypeEnum.DICTATORSHIP) || game.getType().equals(GameTypeEnum.CLASSIC)) && !player.equals(table.getCurrentPresident()))
            throw new PlayerCannotVoteCardException();

        // Check if the player already voted
        if (table.getVotedCards().stream().anyMatch(votedCard -> votedCard.getPlayer().getId().equals(player.getId())))
            throw new PlayerAlreadyVotedCardException();

        // Check if the card have been played this round
        if (table.getPlayedCards().stream().noneMatch(playedCard -> playedCard.getCard().getId().equals(card.getId())))
            throw new CardNotPlayedException();

        // Set the player vote
        VotedCard votedCard = new VotedCard();
        votedCard.setTable(table);
        votedCard.setPlayer(player);
        votedCard.setCard(card);
        table.getVotedCards().add(votedCard);

        // Check we need to end the round
        if (checkIfEveryoneHaveVotedACard(game, table)) {
            table.setStatus(TableStatusEnum.ENDING);

            incrementMostVotedCardPlayerPoints(game);
        }

        return tableDao.update(table);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public void setNextBlackCard(Table table, Card nextBlackCard) {
        logger.debug("Setting next black card to table {}", table);

        table.setCurrentBlackCard(nextBlackCard);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public PlayedCard getMostVotedCard(long gameId) {
        logger.debug("Getting most voted card of the table of the game {}", gameId);

        return tableDao.getMostVotedCard(gameId);
    }

    private void selectPlayerForRoundPresident(Game game) {
        int playerIndex = getCurrentPresidentIndex(game);

        if (playerIndex + 1 < game.getPlayers().size()) {
            playerIndex += 1;
        } else {
            playerIndex = 0;
        }

        game.getTable().setCurrentPresident(game.getPlayers().get(playerIndex));
    }

    private void incrementMostVotedCardPlayerPoints(Game game) {
        PlayedCard mostVotedCard = tableDao.getMostVotedCard(game.getId());
        Player mostVotedCardPlayer = mostVotedCard.getPlayer();
        mostVotedCardPlayer.setPoints(mostVotedCardPlayer.getPoints() + 1);
    }

    private int getCurrentPresidentIndex(Game game) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getId().equals(game.getTable().getCurrentPresident().getId()))
                return i;
        }

        return 0;
    }

    private boolean checkIfEveryoneHavePlayedACard(Game game, Table table) {
        int cardsNeededToVote = game.getPlayers().size();
        if (game.getType() == GameTypeEnum.DICTATORSHIP || game.getType() == GameTypeEnum.CLASSIC) cardsNeededToVote--;

        return table.getPlayedCards().size() == cardsNeededToVote;
    }

    private boolean checkIfEveryoneHaveVotedACard(Game game, Table table) {
        int cardsNeededToEnd = game.getPlayers().size();
        if (game.getType() == GameTypeEnum.DICTATORSHIP || game.getType() == GameTypeEnum.CLASSIC) cardsNeededToEnd = 1;

        return table.getVotedCards().size() == cardsNeededToEnd;
    }

}
