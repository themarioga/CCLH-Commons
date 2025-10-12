package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.models.*;

public interface RoundService {

    Round createRound(Game game, int roundNumber);

    void deleteRound(Round round);

    Round addCardToPlayedCards(Round round, Player player, Card card);

    Round voteCard(Round round, Player player, Card card);

    void setNextBlackCard(Round round, Card nextBlackCard);

    PlayedCard getMostVotedCard(long gameId);

    boolean checkIfEveryoneHavePlayedACard(Round round);

    boolean checkIfEveryoneHaveVotedACard(Round round);

}
