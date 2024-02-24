package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.*;

public interface TableService {

    Table create(Game game);

    Table startRound(Game game);

    Table endRound(Game game);

    Table playCard(Game game, Player player, Card card);

    Table voteCard(Game game, Player player, Card card);

    void setNextBlackCard(Table table, Card nextBlackCard);

    PlayedCard getMostVotedCard(long gameId);

}
