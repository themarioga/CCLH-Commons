package org.themarioga.game.cah.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Player")
public class Player extends org.themarioga.game.commons.models.Player implements Serializable {

    @Column
    private Integer points;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayerHandCard> hand = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayedCard> playedCards = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<VotedCard> votedCards = new ArrayList<>(0);

    public List<PlayerHandCard> getHand() {
        return hand;
    }

    public void setHand(List<PlayerHandCard> hand) {
        this.hand = hand;
    }

    public List<PlayedCard> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<PlayedCard> playedCards) {
        this.playedCards = playedCards;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<VotedCard> getVotedCards() {
        return votedCards;
    }

    public void setVotedCards(List<VotedCard> votedCards) {
        this.votedCards = votedCards;
    }

    @Override
    public String toString() {
        return "Player{" + super.toString() + ", points=" + points + '}';
    }
}
