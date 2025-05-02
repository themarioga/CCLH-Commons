package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.models.Game;

public interface GameService extends org.themarioga.game.commons.services.intf.GameService {

    Game create(long roomId, String roomName, long creatorId);

    Game setVotationMode(Game game, VotationModeEnum type);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setNumberOfPointsToWin(Game game, int numberOfCards);

    Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd);

    Game setDictionary(Game game, long dictionaryId);

    Game startRound(Game game);

    void transferCardsFromDictionaryToDeck(Game game);

}
