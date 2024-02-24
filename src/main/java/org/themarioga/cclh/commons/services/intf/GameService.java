package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.*;

public interface GameService {

    Game create(long roomId, String roomName, long creatorId);

    Game delete(Game game);

    Game setType(Game game, GameTypeEnum type);

    Game setNumberOfCardsToWin(Game game, int numberOfCardsToWin);

    Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setDictionary(Game game, long dictionaryId);

    Game addPlayer(Game game, Player player);

    Game leaveGame(Game game, long userId);

    Game startGame(Game game);

    Game startRound(Game game);

    Game endRound(Game game);

    Game voteForDeletion(Game game, long userId);

    Game playCard(Game game, long userId, long cardId);

    Game voteForCard(Game game, long userId, long cardId);

    Game getByRoom(Room room);

    Game getByRoomId(long roomId);

    PlayedCard getMostVotedCard(long gameId);

}
