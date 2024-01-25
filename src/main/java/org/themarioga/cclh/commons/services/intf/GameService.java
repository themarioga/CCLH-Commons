package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;

public interface GameService {

    Game create(long roomId, String roomName, long creatorId);

    Game delete(Game game);

    Game setType(Game game, GameTypeEnum type);

    Game setNumberOfCardsToWin(Game game, int numberOfCardsToWin);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setDeck(Game game, long deckId);

    Game addPlayer(Game game, long userId);

    Game leaveGame(Game game, long userId);

    Game startGame(Game game);

    Game startRound(Game game);

    Game endRound(Game game);

    Game voteForDeletion(Game game, long userId);

    Game playCard(Game game, long userId, long cardId);

    Game voteForCard(Game game, long userId, long cardId);

    Game getByRoom(Room room);

    Game getByRoomId(long roomId);

}
