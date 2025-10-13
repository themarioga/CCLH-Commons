package org.themarioga.game.cah.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.RoundStatusEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.exceptions.player.PlayerCannotDrawCardException;
import org.themarioga.game.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.cah.services.intf.CCLHService;
import org.themarioga.game.cah.services.intf.model.GameService;
import org.themarioga.game.cah.services.intf.model.PlayerService;
import org.themarioga.game.cah.services.intf.model.RoundService;
import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.enums.GameStatusEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;
import org.themarioga.game.commons.exceptions.game.*;
import org.themarioga.game.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.game.commons.exceptions.room.RoomAlreadyExistsException;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.commons.services.intf.ConfigurationService;
import org.themarioga.game.commons.services.intf.RoomService;
import org.themarioga.game.commons.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class CCLHServiceImpl implements CCLHService {

    private final Logger logger = LoggerFactory.getLogger(CCLHServiceImpl.class);

    private final RoomService roomService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final ConfigurationService configurationService;

    @Autowired
    public CCLHServiceImpl(RoomService roomService, GameService gameService, PlayerService playerService, RoundService roundService, ConfigurationService configurationService) {
        this.roomService = roomService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.roundService = roundService;
        this.configurationService = configurationService;
    }

    @Override
    public Game createGame(String roomName, User creator) {
        logger.debug("Creating game for room {} by user {}", roomName, creator);

        // Check roomName exists
        Assert.assertNotNull(roomName, ErrorEnum.ROOM_NOT_FOUND);

        // Check creator exists
        Assert.assertNotNull(creator, ErrorEnum.USER_NOT_FOUND);

        // Create or load room
        Room room;
        try {
            room = roomService.createOrReactivate(roomName);
        } catch (RoomAlreadyExistsException e) {
            room = roomService.getByName(roomName);
        }

        // Create the game
        Game game = gameService.create(room, creator);

        // Add the game creator player
        Player player = playerService.create(game, creator);
        game.getPlayers().add(player);

        return gameService.update(game);
    }

    @Override
    public Game setVotationMode(Room room, VotationModeEnum type) {
        logger.debug("Setting VotationMode {} to room {}", type, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return gameService.setVotationMode(game, type);
    }

    @Override
    public Game setMaxNumberOfPlayers(Room room, int maxNumberOfPlayers) {
        logger.debug("Setting MaxNumberOfPlayers {} to room {}", maxNumberOfPlayers, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return gameService.setMaxNumberOfPlayers(game, maxNumberOfPlayers);
    }

    @Override
    public Game setNumberOfPointsToWin(Room room, int numberOfCards) {
        logger.debug("Setting NumberOfPointsToWin {} to room {}", numberOfCards, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return gameService.setNumberOfPointsToWin(game, numberOfCards);
    }

    @Override
    public Game setNumberOfRoundsToEnd(Room room, int numberOfRoundsToEnd) {
        logger.debug("Setting NumberOfRoundsToEnd {} to room {}", numberOfRoundsToEnd, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return gameService.setNumberOfRoundsToEnd(game, numberOfRoundsToEnd);
    }

    @Override
    public Game setDictionary(Room room, Dictionary dictionary) {
        logger.debug("Setting Dictionary {} to room {}", dictionary, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check dictionary exists
        Assert.assertNotNull(dictionary, ErrorEnum.DICTIONARY_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return gameService.setDictionary(game, dictionary);
    }

    @Override
    public Game deleteGameByCreator(Room room, User creator) {
        logger.debug("Deleting game from room {} by user {}", room, creator);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check creator exists
        Assert.assertNotNull(creator, ErrorEnum.USER_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Checking if user trying to delete is the creator
        if (!game.getCreator().equals(creator)) throw new GameNotYoursException();

        // Delete the game
        gameService.delete(game);

        return game;
    }

    @Override
    public Game addPlayer(Room room, User user) {
        logger.debug("Adding player {} to game from room {}", user, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Create the player
        Player player = playerService.create(game, user);

        // Check if player has been created successfully
        if (player == null) throw new PlayerDoesntExistsException();

        // Add player to the game
        return gameService.addPlayer(game, player);
    }

    @Override
    public Game removePlayer(Room room, User user) {
        logger.debug("Removing player {} from game in room {}", user, room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Get the player
        Player player = playerService.findPlayerByGameAndUser(game, user);

        // Check if player has been created successfully
        if (player == null) throw new PlayerDoesntExistsException();

        // Remove the player from the game
        game = gameService.removePlayer(game, player);

        // Delete the player
        playerService.delete(player);

        return game;
    }

    @Override
    public Game startGame(Room room) {
        logger.debug("Starting game from room {}", room);

        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Start the game
        game = gameService.startGame(game);

        // Check if the game started correctly
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Start a round
        startRound(game);

        return gameService.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game playCard(Room room, User user, Card card) {
        logger.debug("User {} playing card {} on room {}", user, card, room);

        // Check game exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Check the status of the game
        if (game.getStatus() == GameStatusEnum.STARTED)
            throw new GameAlreadyStartedException();

        // Get the player
        Player player = playerService.findPlayerByGameAndUser(game, user);

        // Add card to round
        roundService.addCardToPlayedCards(game.getCurrentRound(), player, card);

        // Remove card from player hand
        playerService.removeCardFromHand(player, card);

        return gameService.update(game);
    }

    @Override
    public Game voteCard(Room room, User user, Card card) {
        logger.debug("User {} voting card {} on room {}", user, card, room);

        // Check game exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // ToDo
        return null;
    }

    @Override
    public Game voteForDeletion(Room room, User user) {
        logger.debug("User {} voting to delete game on room {}", user, room);

        // Check game exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Check user exists
        Assert.assertNotNull(user, ErrorEnum.USER_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        // Get the player
        Player player = playerService.findPlayerByGameAndUser(game, user);

        // Check user is playing this game
        if (player == null)
            throw new PlayerDoesntExistsException();

        return gameService.voteForDeletion(game, player);
    }

    public void startRound(Game game) {
        logger.debug("Starting round on game {}", game);

        // Check if there is already a round and delete it
        if (game.getCurrentRound() != null) {
            roundService.deleteRound(game.getCurrentRound());
        }

        // Create next round
        Round round = roundService.createRound(game, gameService.getNextRoundNumber(game));

        gameService.setCurrentRound(game, round);

        // Fill player hands
        transferWhiteCardsFromGameDeckToPlayersHands(game);
    }

    private void endRound(Game game) {
        logger.debug("Ending round on game {}", game);

        // Check if the game is started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // If there are not round started it have no sense to end it
        if (game.getCurrentRound() == null)
            throw new RoundWrongStatusException();

        // If round is not ending you can't end it
        if (game.getCurrentRound().getStatus() != RoundStatusEnum.ENDING)
            throw new RoundWrongStatusException();

        // Check if game is ended
        if (checkIfGameEnded(game)) {
            // Set the ended status
            game.setStatus(GameStatusEnum.ENDING);
        }
    }

    private void transferWhiteCardsFromGameDeckToPlayersHands(Game game) {
        logger.debug("Transerir cartas blancas del mazo del juego a los jugadores en el juego: {}", game);

        for (Player player : game.getPlayers()) {
            int numberCardsNeedToFillHand = getDefaultMaxNumberOfCardsInHand() - player.getHand().size();

            if (numberCardsNeedToFillHand < 0 || numberCardsNeedToFillHand > getDefaultMaxNumberOfCardsInHand())
                throw new PlayerCannotDrawCardException();

            List<Card> cardsToTransfer = game.getWhiteCardsDeck().subList(0, numberCardsNeedToFillHand);
            playerService.insertWhiteCardsIntoPlayerHand(player, cardsToTransfer);

            game.getWhiteCardsDeck().removeAll(cardsToTransfer);
        }
    }

    private boolean checkIfGameEnded(Game game) {
        if (game.getPunctuationMode().equals(PunctuationModeEnum.ROUNDS)) {
            return Objects.equals(game.getCurrentRound().getRoundNumber(), game.getNumberOfRounds());
        } else if (game.getPunctuationMode().equals(PunctuationModeEnum.POINTS)) {
            for (Player player : game.getPlayers()) {
                if (Objects.equals(player.getPoints(), game.getNumberOfPointsToWin()))
                    return true;
            }

            return false;
        }

        throw new GameNotEndingException();
    }

    private int getDefaultMaxNumberOfCardsInHand() {
        return Integer.parseInt(configurationService.getConfiguration("game_default_number_cards_in_hand"));
    }

}
