package org.themarioga.game.cah.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.game.cah.dao.intf.GameDao;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.dictionary.DictionaryDoesntExistsException;
import org.themarioga.game.cah.exceptions.game.GameAlreadyFilledException;
import org.themarioga.game.cah.exceptions.game.GameNotFilledException;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.services.intf.GameService;
import org.themarioga.game.cah.services.intf.DictionaryService;
import org.themarioga.game.cah.services.intf.RoundService;
import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.GameAlreadyExistsException;
import org.themarioga.game.commons.exceptions.game.GameAlreadyStartedException;
import org.themarioga.game.commons.exceptions.game.GameCreatorCannotLeaveException;
import org.themarioga.game.commons.exceptions.game.GameNotStartedException;
import org.themarioga.game.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.game.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.game.commons.models.Player;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.ConfigurationService;
import org.themarioga.game.commons.services.intf.PlayerService;
import org.themarioga.game.commons.services.intf.RoomService;
import org.themarioga.game.commons.services.intf.UserService;
import org.themarioga.game.commons.util.Assert;

import java.util.Date;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameDao gameDao;
    private final UserService userService;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final DictionaryService dictionaryService;
    private final ConfigurationService configurationService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, UserService userService, RoomService roomService, PlayerService playerService, RoundService roundService, DictionaryService dictionaryService, ConfigurationService configurationService) {
        this.gameDao = gameDao;
        this.userService = userService;
        this.roomService = roomService;
        this.playerService = playerService;
        this.roundService = roundService;
        this.dictionaryService = dictionaryService;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game create(long roomId, String roomName, long creatorId) {
        logger.debug("Creating game for room {}", roomId);

        // Create or load room
        Room room = roomService.createOrReactivate(roomId, roomName);

        // Check if a game already exists in this room
        if (gameDao.countByRoom(roomService.getById(roomId)) > 0) throw new GameAlreadyExistsException();

        // Check if this user already have a running game
        if (gameDao.countByCreator(userService.getById(creatorId)) > 0) throw new GameAlreadyExistsException();

        // Get the creator
        User creator = userService.getById(creatorId);

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

        return gameDao.create(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game delete(org.themarioga.game.commons.models.Game game) {
        logger.debug("Deleting game: {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        gameDao.delete((Game) game);

        return game;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game setStatus(org.themarioga.game.commons.models.Game game, GameStatusEnum gameStatusEnum) {
        logger.debug("Setting status {} to game: {}", gameStatusEnum, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Set the status
        game.setStatus(gameStatusEnum);

        return gameDao.update((Game) game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setVotationMode(Game game, VotationModeEnum type) {
        logger.debug("Setting votation mode {} to game {}", type, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the type to game
        game.setVotationMode(type);

        return gameDao.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers) {
        logger.debug("Setting max number of players {} to game {}", maxNumberOfPlayers, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Check if the game has more players than the max we want to set
        if (game.getMaxNumberOfPlayers() < game.getPlayers().size())
            throw new GameAlreadyFilledException();

        // Set the max number of players
        game.setMaxNumberOfPlayers(maxNumberOfPlayers);

        return gameDao.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfPointsToWin(Game game, int numberOfCards) {
        logger.debug("Setting number of cards {} to game {}", numberOfCards, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationMode(PunctuationModeEnum.POINTS);

        // Set the number of cards to win
        game.setNumberOfPointsToWin(numberOfCards);

        return gameDao.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd) {
        logger.debug("Setting number of rounds {} to game {}", numberOfRoundsToEnd, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationMode(PunctuationModeEnum.ROUNDS);

        // Set the number of cards to win
        game.setNumberOfRounds(numberOfRoundsToEnd);

        return gameDao.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setDictionary(Game game, long dictionaryId) {
        logger.debug("Setting dictionary {} to game {}", dictionaryId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Find the dictionary
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId);

        // Check if the dictionary exists
        if (dictionary == null) throw new DictionaryDoesntExistsException();

        // Set the dictionary
        game.setDictionary(dictionary);

        return gameDao.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game addPlayer(org.themarioga.game.commons.models.Game game, Player player) {
        logger.debug("Adding player {} to game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Add player to game
        game.getPlayers().add(player);

        return gameDao.update((Game) game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game removePlayer(org.themarioga.game.commons.models.Game game, Player player) {
        logger.debug("Removing player from user {} in game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Game creator cannor leave
        if (Objects.equals(game.getCreator().getId(), player.getUser().getId()))
            throw new GameCreatorCannotLeaveException();

        // Check if the user is not playing
        if (game.getPlayers().stream().noneMatch(p -> Objects.equals(p.getId(), player.getId())))
            throw new PlayerDoesntExistsException();

        // Remove a player
        game.getPlayers().removeIf(p -> Objects.equals(p.getId(), player.getId()));

        return gameDao.update((Game) game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game startGame(org.themarioga.game.commons.models.Game game) {
        logger.debug("Starting game in room {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Change game status
        game.setStatus(GameStatusEnum.STARTED);

        return gameDao.update((Game) game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game endGame(org.themarioga.game.commons.models.Game game) {
        logger.debug("Ending game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Set the ended status
        game.setStatus(GameStatusEnum.ENDING);

        return gameDao.update((Game) game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game voteForDeletion(org.themarioga.game.commons.models.Game game, long userId) {
        logger.debug("Player {} vote for the deletion of the game {}", userId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Get the player
        Player player = playerService.findByUserId(userId);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Check player not voted already
        if (game.getDeletionVotes().contains(player))
            throw new PlayerAlreadyVotedDeleteException();

        // Add deletion votes
        game.getDeletionVotes().add(player);

        // If more than half of the game players vote to delete the game...
        if (game.getStatus().equals(GameStatusEnum.STARTED) && game.getDeletionVotes().size() >= ((game.getPlayers().size() / 2) + 1)) {
            game.setStatus(GameStatusEnum.DELETING);
        }

        return gameDao.update((Game) game);
    }

    @Override
    public Game startRound(Game game) {
        logger.debug("Starting round on game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the number of players in the game
        if (game.getPlayers().size() < getMinNumberOfPlayers())
            throw new GameNotFilledException();

        // Create the next round
        if (game.getCurrentRound() == null) { // If it's a new round
            game.setCurrentRound(roundService.createRound(game, 0));
        } else {
            game.setCurrentRound(roundService.createRound(game, game.getCurrentRound().getRoundNumber() + 1));
        }

        return gameDao.update(game);
    }

    @Override
    public void transferCardsFromDictionaryToDeck(Game game) {
        logger.debug("Transferring cards from dictionary to deck in game {}", game);

        gameDao.transferCardsFromDictionaryToDeck(game);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game getByRoom(Room room) {
        logger.debug("Getting game with room: {}", room);

        return gameDao.getByRoom(room);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public org.themarioga.game.commons.models.Game getByRoomId(long roomId) {
        logger.debug("Getting game with room id: {}", roomId);

        return gameDao.getByRoom(roomService.getById(roomId));
    }

    private VotationModeEnum getDefaultGameMode() {
        return VotationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_type")));
    }

    private PunctuationModeEnum getDefaultGamePunctuationType() {
        return PunctuationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_punctuation_type")));
    }

    private int getDefaultGameLength() {
        return Integer.parseInt(configurationService.getConfiguration("game_default_game_legth"));
    }

    private int getMinNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_min_number_of_players"));
    }

    private int getDefaultMaxNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_max_number_of_players"));
    }

}
