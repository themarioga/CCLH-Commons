package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.models.*;

public interface GameService extends org.themarioga.game.commons.services.intf.GameService<Game, Player> {

    Game setVotationMode(Game game, VotationModeEnum type);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setNumberOfPointsToWin(Game game, int numberOfCards);

    Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd);

    Game setDictionary(Game game, Dictionary dictionary);

    Game setCurrentRound(Game game, Round round);

    int getNextRoundNumber(Game game);

}
