package org.themarioga.engine.cah.models.game;

import jakarta.persistence.*;
import org.themarioga.engine.cah.models.dictionaries.Card;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class VotedCard implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Round round;

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Player player;

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Card card;

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
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
        return Objects.equals(round, votedCard.round) && Objects.equals(player, votedCard.player) && Objects.equals(card, votedCard.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, player, card);
    }

}
