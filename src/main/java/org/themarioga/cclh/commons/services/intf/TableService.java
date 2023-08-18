package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;

public interface TableService {

    Table create(Table table);

    Table update(Table table);

    void delete(Table table);

    void deleteById(long id);

    Table findOne(long id);

    void addBlackCardsToTableDeck(Game game);

    void transferCardFromDeckToTable(Table table);

    void addWhiteCardsToPlayersDecks(Game game);

    PlayedCard getMostVotedCard(Long gameId);
}
