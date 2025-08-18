package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.enums.VotationModeEnum;
import org.themarioga.game.cah.models.Card;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.Game;
import org.themarioga.game.commons.models.User;

public interface GameService extends org.themarioga.game.commons.services.intf.GameService<Game> {

    Game setVotationMode(Game game, VotationModeEnum type);

    Game setMaxNumberOfPlayers(Game game, int maxNumberOfPlayers);

    Game setNumberOfPointsToWin(Game game, int numberOfCards);

    Game setNumberOfRoundsToEnd(Game game, int numberOfRoundsToEnd);

    Game setDictionary(Game game, Dictionary dictionary);

    Game startRound(Game game);

    Game endRound(Game game);

    Game playCard(Game game, User user, Card card);

}
