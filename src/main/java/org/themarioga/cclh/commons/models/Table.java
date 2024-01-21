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
    @Column(name = "game_id")
    private Long gameId;
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
    private List<PlayerVote> playerVotes = new ArrayList<>(0);

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

    public List<PlayerVote> getPlayerVotes() {
        return playerVotes;
    }

    public void setPlayerVotes(List<PlayerVote> playerVotes) {
        this.playerVotes = playerVotes;
    }

    @Override
    public String toString() {
        return "GameStatus{" + "gameId=" + gameId + ", status=" + status + ", currentRoundNumber=" + currentRoundNumber + '}';
    }
}
