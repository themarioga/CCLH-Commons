package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;

public interface GameService {

    Game create(long roomId, String roomName, long ownerId, long creatorId);

    Game setType(Game game, GameTypeEnum type);

    Game setNumberOfCardsToWin(Game game, int numberOfCards);

    Game setDictionary(Game game, long dictionaryId);

    Game startGame(Game game);

    Game startRound(Game game);

    Game getByRoomId(long id);

}
