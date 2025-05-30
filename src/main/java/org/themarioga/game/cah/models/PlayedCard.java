package org.themarioga.game.cah.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class PlayedCard implements Serializable {

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
        PlayedCard that = (PlayedCard) o;
        return Objects.equals(round, that.round) && Objects.equals(player, that.player) && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, player, card);
    }

}
