package org.themarioga.engine.cah.services.intf;

import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.dictionaries.Dictionary;
import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.commons.models.Room;
import org.themarioga.engine.commons.models.User;

public interface CAHService {

    Game createGame(String roomName, User creator);

    Game setVotationMode(Room room, VotationModeEnum type);

    Game setMaxNumberOfPlayers(Room room, int maxNumberOfPlayers);

    Game setNumberOfPointsToWin(Room room, int numberOfCards);

    Game setNumberOfRoundsToEnd(Room room, int numberOfRoundsToEnd);

    Game setDictionary(Room room, Dictionary dictionary);

    Game deleteGameByCreator(Room room, User creator);

    Game addPlayer(Room room, User user);

	Game kickPlayer(Room room, User userWhoKicks, User userKicked);

	Game leavePlayer(Room room, User user);

	Game voteForDeletion(Room room, User user);

	Game startGame(Room room);

    Game playCard(Room room, User user, Card card);

    Game voteCard(Room room, User user, Card card);

}
