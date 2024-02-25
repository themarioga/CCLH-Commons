package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_game_deck")
public class GameDeckCard implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
        GameDeckCard that = (GameDeckCard) o;
        return Objects.equals(game, that.game) && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, card);
    }

}
