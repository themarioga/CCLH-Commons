package org.themarioga.engine.cah.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.engine.cah.enums.PunctuationModeEnum;
import org.themarioga.engine.cah.enums.RoundStatusEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.exceptions.player.PlayerCannotDrawCardException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.game.Round;
import org.themarioga.engine.cah.services.intf.CAHService;
import org.themarioga.engine.cah.services.intf.game.GameService;
import org.themarioga.engine.cah.services.intf.game.PlayerService;
import org.themarioga.engine.cah.services.intf.game.RoundService;
import org.themarioga.engine.commons.enums.ErrorEnum;
import org.themarioga.engine.commons.enums.GameStatusEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;
import org.themarioga.engine.commons.exceptions.game.*;
import org.themarioga.engine.commons.exceptions.player.PlayerDoesntExistsException;
import org.themarioga.engine.commons.exceptions.room.RoomAlreadyExistsException;
import org.themarioga.engine.commons.exceptions.user.UserDoesntExistsException;
import org.themarioga.engine.commons.models.Room;
import org.themarioga.engine.commons.models.User;
import org.themarioga.engine.commons.services.intf.ConfigurationService;
import org.themarioga.engine.commons.services.intf.RoomService;
import org.themarioga.engine.commons.util.Assert;
import org.themarioga.engine.commons.util.SessionUtil;

import java.util.List;
import java.util.Objects;

@Service
public class CAHServiceImpl implements CAHService {

    private final Logger logger = LoggerFactory.getLogger(CAHServiceImpl.class);

    private final RoomService roomService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final ConfigurationService configurationService;

    @Autowired
    public CAHServiceImpl(RoomService roomService, GameService gameService, PlayerService playerService, RoundService roundService, ConfigurationService configurationService) {
        this.roomService = roomService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.roundService = roundService;
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game createGame(String roomName) {
        logger.debug("Creating game for room {}", roomName);

        // Check roomName exists
        Assert.assertNotNull(roomName, ErrorEnum.ROOM_NOT_FOUND);

        // Obtenemos el usuario de la sesión y comprobamos que existe
        User creator = getSessionUser();

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setVotationMode(Room room, VotationModeEnum type) {
        logger.debug("Setting VotationMode {} to room {}", type, room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        return gameService.setVotationMode(game, type);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setMaxNumberOfPlayers(Room room, int maxNumberOfPlayers) {
        logger.debug("Setting MaxNumberOfPlayers {} to room {}", maxNumberOfPlayers, room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        return gameService.setMaxNumberOfPlayers(game, maxNumberOfPlayers);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfPointsToWin(Room room, int numberOfCards) {
        logger.debug("Setting NumberOfPointsToWin {} to room {}", numberOfCards, room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        return gameService.setNumberOfPointsToWin(game, numberOfCards);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setNumberOfRoundsToEnd(Room room, int numberOfRoundsToEnd) {
        logger.debug("Setting NumberOfRoundsToEnd {} to room {}", numberOfRoundsToEnd, room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        return gameService.setNumberOfRoundsToEnd(game, numberOfRoundsToEnd);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game setDictionary(Room room, Dictionary dictionary) {
        logger.debug("Setting Dictionary {} to room {}", dictionary, room);

        // Check dictionary exists
        Assert.assertNotNull(dictionary, ErrorEnum.DICTIONARY_NOT_FOUND);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        return gameService.setDictionary(game, dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game deleteGameByCreator(Room room) {
        logger.debug("Deleting game from room {}", room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        // Delete the game
        gameService.delete(game);

        return game;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game addPlayer(Room room) {
        logger.debug("Adding player to game from room {}", room);

        // Get the game
        Game game = getGameByRoom(room);

        // Obtenemos el usuario de la sesión y comprobamos que existe
        User user = getSessionUser();

        // Create the player
        Player player = playerService.create(game, user);

        // Add player to the game
        return gameService.addPlayer(game, player);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game kickPlayer(Room room, User userKicked) {
        logger.debug("Kicking player {} from game in room {}", userKicked, room);

        // Check userKicked exists
        Assert.assertNotNull(userKicked, ErrorEnum.USER_NOT_FOUND);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        // Try to get the player for this game and user
        Player player = playerService.findPlayerByGameAndUser(game, userKicked);

        // Check if player has been created successfully
        if (player == null)
            throw new PlayerDoesntExistsException();

        // Remove the player from the game
        game = gameService.removePlayer(game, player);

        // Delete the player
        playerService.delete(player);

        return game;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game leavePlayer(Room room) {
        logger.debug("Leaving game in room {}", room);

        // Get the game
        Game game = getGameByRoom(room);

        // Get the player
        Player player = getPlayerBySessionUserAndGame(game);

        // Remove the player from the game
        game = gameService.removePlayer(game, player);

        // Delete the player
        playerService.delete(player);

        return game;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game voteForDeletion(Room room) {
        logger.debug("User voting to delete game on room {}", room);

        // Get the game
        Game game = getGameByRoom(room);

        // Get the player
        Player player = getPlayerBySessionUserAndGame(game);

        // Vote for deletion
        game = gameService.voteForDeletion(game, player);

        return game;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game startGame(Room room) {
        logger.debug("Starting game from room {}", room);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the user performing the action is the creator
        checkSessionUserIsCreator(game);

        // Start the game
        game = gameService.startGame(game);

        // Start the first round
        startRound(game);

        return gameService.update(game);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Game playCard(Room room, Card card) {
        logger.debug("User playing card {} on room {}", card, room);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the game have already started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Get the player
        Player player = getPlayerBySessionUserAndGame(game);

        // Add card to round
        roundService.addCardToPlayedCards(game.getCurrentRound(), player, card);

        // Remove card from player hand
        playerService.removeCardFromHand(player, card);

        // Set status to voting if everyone have played
        if (roundService.checkIfEveryoneHavePlayedACard(game.getCurrentRound())) {
            roundService.setStatus(game.getCurrentRound(), RoundStatusEnum.VOTING);
        }

        return gameService.update(game);
    }

    @Override
    public Game voteCard(Room room, Card card) {
        logger.debug("User voting card {} on room {}", card, room);

        // Check user exists
        Assert.assertNotNull(card, ErrorEnum.CARD_NOT_FOUND);

        // Get the game
        Game game = getGameByRoom(room);

        // Check the game have already started
        if (game.getStatus() != GameStatusEnum.STARTED)
            throw new GameNotStartedException();

        // Get the player
        Player player = getPlayerBySessionUserAndGame(game);

        // Vote card
        roundService.voteCard(game.getCurrentRound(), player, card);

        // Check if everyone have voted a card
        if (roundService.checkIfEveryoneHaveVotedACard(game.getCurrentRound())) {
            endRound(game);
        }

        return gameService.update(game);
    }

    // ///////////// Helpers //////////////////

    private User getSessionUser() {
        User user = SessionUtil.getCurrentUser();
        if (user == null)
            throw new UserDoesntExistsException();
        return user;
    }

    private Game getGameByRoom(Room room) {
        // Check room exists
        Assert.assertNotNull(room, ErrorEnum.ROOM_NOT_FOUND);

        // Get the game from this room
        Game game = gameService.getByRoom(room);

        // Check if the game exists
        if (game == null) throw new GameDoesntExistsException();

        return game;
    }

    private void checkSessionUserIsCreator(Game game) {
        // Obtenemos el usuario de la sesión y comprobamos que existe
        User creator = getSessionUser();

        // Check if the user performing the action is the creator of the game
        if (!game.getCreator().getId().equals(creator.getId()))
            throw new GameOnlyCreatorCanPerformActionException();
    }

    private Player getPlayerBySessionUserAndGame(Game game) {
        // Obtenemos el usuario de la sesión y comprobamos que existe
        User user = getSessionUser();

        // Try to get the player for this game and user
        Player player = playerService.findPlayerByGameAndUser(game, user);

        // Check if player has been created successfully
        if (player == null) throw new PlayerDoesntExistsException();

        return player;
    }

    private void startRound(Game game) {
        logger.debug("Starting round on game {}", game);

        // Check if there is already a round and delete it
        if (game.getCurrentRound() != null) {
            roundService.deleteRound(game.getCurrentRound());
        }

        // Create next round
        Round round = roundService.createRound(game, gameService.getNextRoundNumber(game));

        // Set the next round
        gameService.setCurrentRound(game, round);

        // Fill player hands
        transferWhiteCardsFromGameDeckToPlayersHands(game);
    }

    private void endRound(Game game) {
        roundService.setStatus(game.getCurrentRound(), RoundStatusEnum.ENDING);

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
            return Objects.equals(game.getCurrentRound().getRoundNumber(), game.getNumberOfRoundsToEnd());
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
