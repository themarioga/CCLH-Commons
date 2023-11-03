package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.GameTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.models.User;

public interface GameService {

    Game create(long roomId, String roomName, long roomOwnerId, long creatorId);

    void delete(long roomId);

    Game setType(Game game, GameTypeEnum type);

    Game setNumberOfCardsToWin(Game game, int numberOfCards);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setDictionary(Game game, long dictionaryId);

    Game addPlayer(Game game, User user);

    Game startGame(Game game);

    Game startRound(Game game);

    Game endRound(Game game);

    void voteForDeletion(Game game, Player player);

    Game getByRoom(Room room);

    Game getByRoomId(long roomId);

}
