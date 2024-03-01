package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.*;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.dictionary.DictionaryDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.game.*;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.cclh.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.services.intf.*;
import org.themarioga.cclh.commons.util.Assert;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameDao gameDao;
    private final UserService userService;
    private final RoomService roomService;
    private final CardService cardService;
    private final TableService tableService;
    private final PlayerService playerService;
    private final DictionaryService dictionaryService;
    private final ConfigurationService configurationService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, UserService userService, RoomService roomService, CardService cardService, PlayerService playerService, TableService tableService, DictionaryService dictionaryService, ConfigurationService configurationService) {
        this.gameDao = gameDao;
        this.userService = userService;
        this.roomService = roomService;
        this.cardService = cardService;
        this.playerService = playerService;
        this.tableService = tableService;
        this.dictionaryService = dictionaryService;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game create(long roomId, String roomName, long creatorId) {
        logger.debug("Creating game in room: {}({})", roomId, roomName);

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
        game.setCreationDate(new Date());
        game.setCreator(creator);
        game.setStatus(GameStatusEnum.CREATED);
        game.setType(getDefaultGameMode());
        game.setPunctuationType(getDefaultGamePunctuationType());
        game.setNumberOfCardsToWin(getDefaultGameLength());
        game.setNumberOfRounds(getDefaultGameLength());
        game.setMaxNumberOfPlayers(getDefaultMaxNumberOfPlayers());
        game.setDictionary(dictionaryService.getDefaultDictionary());

        return gameDao.create(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game delete(Game game) {
        logger.debug("Deleting game: {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        gameDao.delete(game);

        return game;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game setType(Game game, GameTypeEnum type) {
        logger.debug("Setting type {} to game {}", type, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the type to game
        game.setType(type);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game setNumberOfCardsToWin(Game game, int numberOfCards) {
        logger.debug("Setting number of cards {} to game {}", numberOfCards, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationType(GamePunctuationTypeEnum.POINTS);

        // Set the number of cards to win
        game.setNumberOfCardsToWin(numberOfCards);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd) {
        logger.debug("Setting number of rounds {} to game {}", numberOfRoundsToEnd, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Set the game punctuation type
        game.setPunctuationType(GamePunctuationTypeEnum.ROUNDS);

        // Set the number of cards to win
        game.setNumberOfRounds(numberOfRoundsToEnd);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers) {
        logger.debug("Setting max number of players {} to game {}", maxNumberOfPlayers, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Check if the game has more players than the max we want to set
        if (game.getMaxNumberOfPlayers() < game.getPlayers().size()) throw new GameAlreadyFilledException();

        // Set the max number of players
        game.setMaxNumberOfPlayers(maxNumberOfPlayers);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game setDictionary(Game game, long dictionaryId) {
        logger.debug("Setting dictionary {} to game {}", dictionaryId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if the game has already started
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Find the dictionary
        Dictionary dictionary = dictionaryService.findOne(dictionaryId);

        // Check if the dictionary exists
        if (dictionary == null) throw new DictionaryDoesntExistsException();

        // Set the dictionary
        game.setDictionary(dictionary);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game addPlayer(Game game, Player player) {
        logger.debug("Adding player {} to game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Check game is not full
        if (game.getPlayers().size() >= game.getMaxNumberOfPlayers())
            throw new GameAlreadyFilledException();

        // Add player to game
        game.getPlayers().add(player);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game removePlayer(Game game, Player player) {
        logger.debug("Removing player from user {} in game {}", player, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

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

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game startGame(Game game) {
        logger.debug("Starting game in room {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Check the number of players in the game
        if (game.getPlayers().size() < getMinNumberOfPlayers())
            throw new GameNotFilledException();

        // Change game status
        game.setStatus(GameStatusEnum.STARTED);

        // Add cards to game deck
        gameDao.transferCardsToGameDeck(game);

        // Create table
        game.setTable(tableService.create(game));

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game startRound(Game game) {
        logger.debug("Starting round for game in room {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Set table for new round
        tableService.startRound(game);

        // Setting the black card for the round
        tableService.setNextBlackCard(game.getTable(), getBlackCardFromGameDeck(game));

        // Add white cards to player hands
        addWhiteCardsToPlayerHands(game);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game endRound(Game game) {
        logger.debug("Ending round for game {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // End table round
        tableService.endRound(game);

        // Send status to ended
        if (checkIfGameIsOver(game)) {
            game.setStatus(GameStatusEnum.ENDED);
        }

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game voteForDeletion(Game game, long userId) {
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

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game playCard(Game game, long userId, long cardId) {
        logger.debug("Player {} used the card {} of the game {}", userId, cardId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Get the player
        Player player = playerService.findByUserId(userId);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Get the card
        Card card = cardService.findOne(cardId);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Let the table finish the job
        tableService.playCard(game, player, card);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game voteForCard(Game game, long userId, long cardId) {
        logger.debug("Player {} vote for the card {} of the game {}", userId, cardId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check if game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Get the player
        Player player = playerService.findByUserId(userId);

        // Check player exists
        Assert.assertNotNull(player, ErrorEnum.PLAYER_NOT_FOUND);

        // Get the carc
        Card card = cardService.findOne(cardId);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Let the table finish the job
        tableService.voteCard(game, player, card);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Game getByRoom(Room room) {
        logger.debug("Getting game with room: {}", room);

        return gameDao.getByRoom(room);
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public Game getByRoomId(long roomId) {
        logger.debug("Getting game with room id: {}", roomId);

        return gameDao.getByRoom(roomService.getById(roomId));
    }

    @Override
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = ApplicationException.class)
    public PlayedCard getMostVotedCard(long gameId) {
        logger.debug("Getting game with room id: {}", gameId);

        return tableService.getMostVotedCard(gameId);
    }

    private Card getBlackCardFromGameDeck(Game game) {
        logger.debug("Getting black card from deck to table {}", game);

        GameDeckCard nextBlackCard = gameDao.getGameDeckCards(game.getId(), 1, CardTypeEnum.BLACK).get(0);
        game.getDeckCards().remove(nextBlackCard);

        return nextBlackCard.getCard();
    }

    private void addWhiteCardsToPlayerHands(Game game) {
        int cardsInHand = Integer.parseInt(configurationService.getConfiguration("game_whitecards_in_hand"));

        for (Player player : game.getPlayers()) {
            if (player.getHand().size() < cardsInHand) {
                int missingCards = Math.min(cardsInHand - player.getHand().size(), player.getGame().getDeckCards().size());

                List<GameDeckCard> cardsToTransfer = new ArrayList<>(gameDao.getGameDeckCards(player.getGame().getId(), missingCards, CardTypeEnum.WHITE));

                playerService.transferWhiteCardsFromGameDeckToPlayerHand(player, cardsToTransfer);

                player.getGame().getDeckCards().removeAll(cardsToTransfer);
            }
        }
    }

    private boolean checkIfGameIsOver(Game game) {
        if (game.getPunctuationType() == GamePunctuationTypeEnum.ROUNDS) {
	        return Objects.equals(game.getTable().getCurrentRoundNumber(), game.getNumberOfRounds());
        } else {
            Optional<Player> maxVotedPlayer = game.getPlayers().stream().max(Comparator.comparing(Player::getPoints));
	        return maxVotedPlayer.isPresent() && Objects.equals(maxVotedPlayer.get().getPoints(), game.getNumberOfCardsToWin());
        }
    }

    private GameTypeEnum getDefaultGameMode() {
        return GameTypeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_type")));
    }

    private GamePunctuationTypeEnum getDefaultGamePunctuationType() {
        return GamePunctuationTypeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_punctuation_type")));
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
