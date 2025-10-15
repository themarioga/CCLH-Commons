package org.themarioga.game.cah.services.intf.model;

import org.themarioga.game.cah.enums.RoundStatusEnum;
import org.themarioga.game.cah.models.*;

public interface RoundService {

    Round createRound(Game game, int roundNumber);

    void deleteRound(Round round);

	Round setStatus(Round round, RoundStatusEnum status);

    Round addCardToPlayedCards(Round round, Player player, Card card);

    Round voteCard(Round round, Player player, Card card);

	Round setNextBlackCard(Round round, Card nextBlackCard);

	VotedCard getMostVotedCard(Round round);

	PlayedCard getPlayedCardByCard(Round round, Card card);

    boolean checkIfEveryoneHavePlayedACard(Round round);

    boolean checkIfEveryoneHaveVotedACard(Round round);

}
