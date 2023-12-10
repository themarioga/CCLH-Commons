package org.themarioga.cclh.commons.services.intf;

import jakarta.transaction.Transactional;
import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;

public interface GameService {

    Game create(long roomId, String roomName, long roomOwnerId, long creatorId);

    Game delete(long roomId);

    Game setType(long roomId, GameTypeEnum type);

    Game setNumberOfCardsToWin(long roomId, int numberOfCardsToWin);

    Game setMaxNumberOfPlayers(long roomId, int maxNumberOfPlayers);

    Game setDictionary(long roomId, long dictionaryId);

    Game addPlayer(long roomId, long userId);

    Game leaveGame(long roomId, long userId);

    Game startGame(long roomId);

    Game startRound(long roomId);

    Game endRound(long roomId);

    Game voteForDeletion(long roomId, long userId);

    Game playCard(long roomId, long userId, long cardId);

    Game voteForCard(long roomId, long userId, long cardId);

    Game getByRoom(Room room);

    Game getByRoomId(long roomId);

}
