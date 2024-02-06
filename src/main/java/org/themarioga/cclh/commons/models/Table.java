package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;
import org.themarioga.cclh.commons.enums.TableStatusEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "t_table")
public class Table implements Serializable {

    @Id
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
    @Column(name = "status", nullable = false)
    private TableStatusEnum status;
    @Column(name = "round_number", nullable = false)
    private Integer currentRoundNumber;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "blackcard_id", referencedColumnName = "id")
    private Card currentBlackCard;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id", referencedColumnName = "id")
    private Player currentPresident;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_table_deck", joinColumns = @JoinColumn(name = "game_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "card_id", nullable = false))
    private List<Card> deck = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private List<PlayedCard> playedCards = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private List<VotedCard> votedCards = new ArrayList<>(0);

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TableStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TableStatusEnum status) {
        this.status = status;
    }

    public Integer getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    public void setCurrentRoundNumber(Integer currentRoundNumber) {
        this.currentRoundNumber = currentRoundNumber;
    }

    public Card getCurrentBlackCard() {
        return currentBlackCard;
    }

    public void setCurrentBlackCard(Card currentBlackCard) {
        this.currentBlackCard = currentBlackCard;
    }

    public Player getCurrentPresident() {
        return currentPresident;
    }

    public void setCurrentPresident(Player currentPresident) {
        this.currentPresident = currentPresident;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
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
    public String toString() {
        return "GameStatus{status=" + status + ", currentRoundNumber=" + currentRoundNumber + '}';
    }
}
