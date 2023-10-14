package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;

public interface TableService {

    Table create(Game game);

    Table startRound(Game game);

    Table endRound(Game game);

    PlayedCard getMostVotedCard(Long gameId);

}
