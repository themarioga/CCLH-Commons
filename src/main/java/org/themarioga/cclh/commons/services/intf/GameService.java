package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.Room;

public interface GameService {

    Game create(long roomId, String roomName, long ownerId, long creatorId);

    void delete(Game game);

    Game setType(Game game, GameTypeEnum type);

    Game setNumberOfCardsToWin(Game game, int numberOfCards);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setDictionary(Game game, long dictionaryId);

    Game startGame(Game game);

    Game startRound(Game game);

    Game endRound(Game game);

    void voteForDeletion(Game game, Player player);

    Game getByRoomId(Room room);

}
