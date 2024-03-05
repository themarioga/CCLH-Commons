package org.themarioga.cclh.commons.services.intf;

import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.enums.TableStatusEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.*;

public interface TableService {

    Table create(Game game);

    Table setStatus(Game game, TableStatusEnum newStatus);

    Table startRound(Game game);

    Table endRound(Game game);

    Table playCard(Game game, Player player, Card card);

    Table voteCard(Game game, Player player, Card card);

    void setNextBlackCard(Table table, Card nextBlackCard);

    PlayedCard getMostVotedCard(long gameId);

    boolean checkIfEveryoneHavePlayedACard(Game game);

    boolean checkIfEveryoneHaveVotedACard(Game game);
}
