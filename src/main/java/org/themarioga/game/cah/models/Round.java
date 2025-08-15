package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.cah.enums.RoundStatusEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Round implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Game game;

    @Column(nullable = false)
    private Integer roundNumber;
    @Column(nullable = false)
    private RoundStatusEnum status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn
    private Card roundBlackCard;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn
    private Player roundPresident;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "round", orphanRemoval = true)
    private List<PlayedCard> playedCards = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "round", orphanRemoval = true)
    private List<VotedCard> votedCards = new ArrayList<>(0);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public RoundStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RoundStatusEnum status) {
        this.status = status;
    }

    public Card getRoundBlackCard() {
        return roundBlackCard;
    }

    public void setRoundBlackCard(Card currentBlackCard) {
        this.roundBlackCard = currentBlackCard;
    }

    public Player getRoundPresident() {
        return roundPresident;
    }

    public void setRoundPresident(Player currentPresident) {
        this.roundPresident = currentPresident;
    }

    public List<PlayedCard> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<PlayedCard> playedCards) {
        this.playedCards = playedCards;
    }

    public List<VotedCard> getVotedCards() {
        return votedCards;
    }

    public void setVotedCards(List<VotedCard> votedCards) {
        this.votedCards = votedCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return Objects.equals(id, round.id) && Objects.equals(game, round.game) && Objects.equals(roundNumber, round.roundNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game, roundNumber);
    }

    @Override
    public String toString() {
        return "Round{" + "id=" + id + ", roundBlackCard=" + roundBlackCard + ", roundNumber=" + roundNumber + ", status=" + status + '}';
    }

}
