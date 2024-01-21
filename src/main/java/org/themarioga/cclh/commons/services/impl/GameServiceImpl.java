package org.themarioga.cclh.commons.services.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.deck.DeckDoesntExistsException;
import org.themarioga.cclh.commons.exceptions.game.*;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyExistsException;
import org.themarioga.cclh.commons.exceptions.player.PlayerAlreadyVotedDeleteException;
import org.themarioga.cclh.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.cclh.commons.models.*;
import org.themarioga.cclh.commons.services.intf.*;
import org.themarioga.cclh.commons.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameDao gameDao;
    private final UserService userService;
    private final RoomService roomService;
    private final CardService cardService;
    private final TableService tableService;
    private final PlayerService playerService;
    private final DeckService deckService;
    private final ConfigurationService configurationService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, UserService userService, RoomService roomService, CardService cardService, PlayerService playerService, TableService tableService, DeckService deckService, ConfigurationService configurationService) {
        this.gameDao = gameDao;
        this.userService = userService;
        this.roomService = roomService;
        this.cardService = cardService;
        this.playerService = playerService;
        this.tableService = tableService;
        this.deckService = deckService;
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

        // Create game
        Game game = new Game();
        game.setRoom(room);
        game.setCreationDate(new Date());
        game.setCreator(userService.getById(creatorId));
        game.setStatus(GameStatusEnum.CREATED);
        game.setType(GameTypeEnum.DEMOCRACY);
        game.setNumberOfCardsToWin(getDefaultNumberCardsToWin());
        game.setMaxNumberOfPlayers(getDefaultMaxNumberOfPlayers());
        game.setDictionary(deckService.getDefaultDictionary());

        return gameDao.create(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game delete(Game game) {
        logger.debug("Deleting game: {}", game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        gameDao.delete(game);

        game.setStatus(GameStatusEnum.DELETED);

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

        // Set the number of cards to win
        game.setNumberOfCardsToWin(numberOfCards);

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
        Deck deck = deckService.findOne(dictionaryId);

        // Check if the dictionary exists
        if (deck == null) throw new DeckDoesntExistsException();

        // Set the dictionary
        game.setDictionary(deck);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game addPlayer(Game game, long userId) {
        logger.debug("Creating player from user {} in game {}", userId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Get the user
        User user = userService.getById(userId);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Check game is not full
        if (game.getPlayers().size() >= game.getMaxNumberOfPlayers())
            throw new GameAlreadyFilledException();

        // Check if the user is already playing
        if (playerService.findByUserId(userId) != null)
            throw new PlayerAlreadyExistsException();

        // Create player
        Player player = playerService.create(game, user);

        // Add player to game
        game.getPlayers().add(player);

        return gameDao.update(game);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    public Game leaveGame(Game game, long userId) {
        logger.debug("Removing player from user {} in game {}", userId, game);

        // Check game exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED) throw new GameAlreadyStartedException();

        // Game creator cannor leave
        if (game.getCreator().getId() == userId) throw new GameCreatorCannotLeaveException();

        // Get the player
        Player player = playerService.findByUserId(userId);

        // Check if the user is not playing
        if (player == null)
            throw new PlayerDoesntExistsException();

        // Remove a player
        game.getPlayers().remove(player);

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

        // Create table
        game.setTable(tableService.create(game));

        // Add cards to players decks
        addWhiteCardsToPlayersDecks(game);

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

        // Add cards to player hands
        for (Player player : game.getPlayers()) {
            playerService.transferCardsFromPlayerDeckToPlayerHand(player);
        }

        // Set table for new round
        tableService.startRound(game);

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
            return delete(game);
        } else {
            return gameDao.update(game);
        }
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

    private int getMinNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_min_number_of_players"));
    }

    private int getDefaultNumberCardsToWin() {
        return Integer.parseInt(configurationService.getConfiguration("game_default_number_cards_to_win"));
    }

    private int getDefaultMaxNumberOfPlayers() {
        return Integer.parseInt(configurationService.getConfiguration("game_max_number_of_players"));
    }

    private void addWhiteCardsToPlayersDecks(Game game) {
        logger.debug("Adding white cards to players in the game {}", game);

        List<Card> cards = new ArrayList<>(cardService.findCardsByDictionaryIdAndType(game.getDictionary(), CardTypeEnum.WHITE));

        Collections.shuffle(cards);

        int cardsPerPlayer = Math.floorDiv(cards.size(), game.getPlayers().size());

        for (Player player : game.getPlayers()) {
            List<Card> playerCards = cards.subList(0, cardsPerPlayer);

            playerService.addCardsToPlayerDeck(player, playerCards);

            cards.removeAll(playerCards);
        }
    }

}
