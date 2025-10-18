package org.themarioga.engine.cah.services.intf.game;

import org.themarioga.engine.cah.enums.RoundStatusEnum;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.*;

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
