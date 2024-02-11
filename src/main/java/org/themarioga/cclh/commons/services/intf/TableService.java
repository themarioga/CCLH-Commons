package org.themarioga.cclh.commons.services.intf;

import jakarta.transaction.Transactional;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.*;

public interface TableService {

    Table create(Game game);

    Table startRound(Game game);

    Table endRound(Game game);

    Table playCard(Game game, Player player, Card card);

    Table voteCard(Game game, Player player, Card card);

    void transferCardsToTableDeck(Game game, CardTypeEnum cardTypeEnum);

    PlayedCard getMostVotedCard(Long gameId);

}
