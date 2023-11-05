package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;

public interface GameService {

    Game create(long roomId, String roomName, long roomOwnerId, long creatorId);

    void delete(long roomId);

    Game setType(long roomId, GameTypeEnum type);

    Game setNumberOfCardsToWin(long roomId, int numberOfCards);

    Game setMaxNumberOfPlayers(long roomId, int N_PLAYERS);

    Game setDictionary(long roomId, long dictionaryId);

    Game addPlayer(long roomId, long userId);

    Game startGame(long roomId);

    Game startRound(long roomId);

    Game endRound(long roomId);

    void voteForDeletion(long roomId, long userId);

    Game getByRoom(Room room);

    Game getByRoomId(long roomId);

}
