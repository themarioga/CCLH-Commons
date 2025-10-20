package org.themarioga.engine.cah.services.intf;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.commons.exceptions.ApplicationException;
import org.themarioga.engine.commons.models.Room;
import org.themarioga.engine.commons.models.User;

public interface CAHService {

    Game createGame(String roomName);

    Game setVotationMode(Room room, VotationModeEnum type);

    Game setMaxNumberOfPlayers(Room room, int maxNumberOfPlayers);

    Game setNumberOfPointsToWin(Room room, int numberOfCards);

    Game setNumberOfRoundsToEnd(Room room, int numberOfRoundsToEnd);

    Game setDictionary(Room room, Dictionary dictionary);

    Game deleteGameByCreator(Room room);

    Game addPlayer(Room room);

    Game kickPlayer(Room room, User userKicked);

    Game leavePlayer(Room room);

    Game voteForDeletion(Room room);

    Game startGame(Room room);

    Game playCard(Room room, Card card);

    Game voteCard(Room room, Card card);

    Game nextRound(Game game);

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
	Player getWinner(Game game);
}
