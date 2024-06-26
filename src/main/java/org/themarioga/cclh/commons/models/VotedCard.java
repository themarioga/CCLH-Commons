package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_table_playervotes")
public class VotedCard implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private Table table;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotedCard votedCard = (VotedCard) o;
        return Objects.equals(table, votedCard.table) && Objects.equals(player, votedCard.player) && Objects.equals(card, votedCard.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, player, card);
    }

}
