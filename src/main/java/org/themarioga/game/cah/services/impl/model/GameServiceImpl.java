package org.themarioga.game.cah.services.impl.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.game.cah.dao.intf.GameDao;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.game.GameAlreadyFilledException;
import org.themarioga.game.cah.exceptions.game.GameNotFilledException;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.Round;
import org.themarioga.game.cah.services.intf.model.DictionaryService;
import org.themarioga.game.cah.services.intf.model.GameService;
import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.*;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.ConfigurationService;
import org.themarioga.game.commons.util.Assert;

import java.util.Date;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameDao gameDao;
    private final DictionaryService dictionaryService;
    private final ConfigurationService configurationService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, DictionaryService dictionaryService, ConfigurationService configurationService) {
        this.gameDao = gameDao;
        this.dictionaryService = dictionaryService;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game create(Room room, User creator) {
        logger.debug("Creating game for room {} by user {}", room, creator);

        // Check if the room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check if a game already exists in this room
        if (gameDao.countByRoom(room) > 0)
            throw new GameAlreadyExistsException();

        // Check if this user already have a running game
        if (gameDao.countByCreator(creator) > 0)
            throw new GameCreatorAlreadyExistsException();

        // Check if the creator exists
        Assert.assertNotNull(creator, ErrorEnum.USER_NOT_FOUND);

        // Create game
        Game game = new Game();
        game.setRoom(room);
        game.setCreator(creator);
        game.setStatus(GameStatusEnum.CREATED);
        game.setCreationDate(new Date());
        game.setVotationMode(getDefaultGameMode());
        game.setPunctuationMode(getDefaultGamePunctuationType());
        game.setNumberOfPointsToWin(getDefaultGameLength());
        game.setNumberOfRounds(getDefaultGameLength());
        game.setMaxNumberOfPlayers(getDefaultMaxNumberOfPlayers());
        game.setDictionary(dictionaryService.getDefaultDictionary());
        game.setCreationDate(new Date());
        game.setMaxNumberOfPlayers(getDefaultMaxNumberOfPlayers());

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game update(Game game) {
        logger.debug("Updating game: {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Game game) {
        logger.debug("Deleting game: {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        gameDao.delete(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setStatus(Game game, GameStatusEnum gameStatusEnum) {
        logger.debug("Setting status {} to game: {}", gameStatusEnum, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Set the status
        game.setStatus(gameStatusEnum);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setVotationMode(Game game, VotationModeEnum type) {
        logger.debug("Setting votation mode {} to game {}", type, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Set the type to game
        game.setVotationMode(type);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers) {
        logger.debug("Setting max number of players {} to game {}", maxNumberOfPlayers, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Check if the game has more players than the max we want to set
        if (maxNumberOfPlayers < game.getPlayers().size())
            throw new GameAlreadyFilledException();

        // Check if max number of players is less than min
        if (maxNumberOfPlayers < getMinNumberOfPlayers())
            throw new GameAlreadyFilledException();

        // Set the max number of players
        game.setMaxNumberOfPlayers(maxNumberOfPlayers);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfPointsToWin(Game game, int numberOfCards) {
        logger.debug("Setting number of cards {} to game {}", numberOfCards, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationMode(PunctuationModeEnum.POINTS);

        // Set the number of cards to win
        game.setNumberOfPointsToWin(numberOfCards);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd) {
        logger.debug("Setting number of rounds {} to game {}", numberOfRoundsToEnd, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationMode(PunctuationModeEnum.ROUNDS);

        // Set the number of cards to win
        game.setNumberOfRounds(numberOfRoundsToEnd);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setDictionary(Game game, Dictionary dictionary) {
        logger.debug("Setting dictionary {} to game {}", dictionary, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check game exists
        Assert.assertNotNull(dictionary, ErrorEnum.DICTIONARY_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Set the dictionary
        game.setDictionary(dictionary);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game addPlayer(Game game, Player player) {
        logger.debug("Adding player {} to game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Check if the game has not started
        if (game.getStatus() != GameStatusEnum.CREATED)
            throw new GameAlreadyStartedException();

        // Check if game is already filled
        if (game.getPlayers().size() + 1 > game.getMaxNumberOfPlayers())
            throw new GameAlreadyFilledException();

        // Add player to game
        game.getPlayers().add(player);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game removePlayer(Game game, Player player) {
        logger.debug("Removing player from player {} in game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Game creator cannor leave
        if (Objects.equals(game.getCreator().getId(), player.getUser().getId()))
            throw new GameCreatorCannotLeaveException();

        // Remove a player
        game.getPlayers().removeIf(p -> Objects.equals(p.getId(), player.getId()));

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game startGame(Game game) {
        logger.debug("Starting game in room {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Check the players are more than min
        if (game.getPlayers().size() < getMinNumberOfPlayers())
            throw new GameNotFilledException();

        // Check the players are less than max
        if (game.getPlayers().size() > game.getMaxNumberOfPlayers())
            throw new GameAlreadyFilledException();

        // Change game status
        game.setStatus(GameStatusEnum.STARTED);

        // Transfer cards to deck
        gameDao.transferCardsFromDictionaryToDeck(game);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void endGame(Game game) {
        logger.debug("Ending game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Only started games can end
        if (game.getStatus() != GameStatusEnum.ENDING)
            throw new GameNotEndingException();

        gameDao.delete(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setCurrentRound(Game game, Round round) {
        logger.debug("Setting current round {} to game {}", round, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the number of players in the game
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Set the current round
        game.setCurrentRound(round);

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public int getNextRoundNumber(Game game) {
        logger.debug("Getting next round number from game {}", game);

        // Determine and return the next round number
        if (game.getCurrentRound() == null) {
            return 0;
        } else {
            return game.getCurrentRound().getRoundNumber() + 1;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game voteForDeletion(Game game, Player player) {
        logger.debug("Player {} vote for the deletion of the game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Check if game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Check user not voted already
        if (game.getDeletionVotes().stream().anyMatch(a -> a.getId().equals(player.getUser().getId())))
            throw new PlayerAlreadyVotedDeleteException();

        // Add deletion votes
        game.getDeletionVotes().add(player.getUser());

        // If more than half of the game players vote to delete the game...
        if (game.getStatus().equals(GameStatusEnum.STARTED) && game.getDeletionVotes().size() >= ((game.getPlayers().size() / 2) + 1)) {
            game.setStatus(GameStatusEnum.DELETING);
        }

        return gameDao.createOrUpdate(game);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Game getByRoom(Room room) {
        logger.debug("Getting game with room: {}", room);

        return (Game) gameDao.getByRoom(room);
    }

    private VotationModeEnum getDefaultGameMode() {
        return VotationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_type")));
    }

    private PunctuationModeEnum getDefaultGamePunctuationType() {
        return PunctuationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_punctuation_type")));
    }

    private int getDefaultGameLength() {
        return Integer.parseInt(configurationService.getConfiguration("game_default_game_length"));
    }

    private int getMinNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_min_number_of_players"));
    }

    private int getDefaultMaxNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_max_number_of_players"));
    }

}
