package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CAHGame")
public class Game extends org.themarioga.game.commons.models.Game implements Serializable {

    @Column(nullable = false)
    private VotationModeEnum votationMode;
    @Column(nullable = false)
    private PunctuationModeEnum punctuationMode;
    @Column(nullable = false)
    private Integer numberOfPointsToWin;
    @Column(nullable = false)
    private Integer numberOfRounds;
    @Column(nullable = false)
    private Integer maxNumberOfPlayers;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Dictionary dictionary;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game", orphanRemoval = true)
    private List<DeckCard> deckCards = new ArrayList<>(0);

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "game", orphanRemoval = true)
    private Round currentRound;

    public VotationModeEnum getVotationMode() {
        return votationMode;
    }

    public void setVotationMode(VotationModeEnum type) {
        this.votationMode = type;
    }

    public PunctuationModeEnum getPunctuationMode() {
        return punctuationMode;
    }

    public void setPunctuationMode(PunctuationModeEnum punctuationType) {
        this.punctuationMode = punctuationType;
    }

    public Integer getNumberOfPointsToWin() {
        return numberOfPointsToWin;
    }

    public void setNumberOfPointsToWin(Integer numberOfCardsToWin) {
        this.numberOfPointsToWin = numberOfCardsToWin;
    }

    public Integer getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Integer numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public List<DeckCard> getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(List<DeckCard> deckCards) {
        this.deckCards = deckCards;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    @Override
    public String toString() {
        return "Game{" + super.toString() + "currentRound=" + currentRound + ", votationMode=" + votationMode + ", punctuationMode=" + punctuationMode + ", numberOfCardsToWin=" + numberOfPointsToWin + ", numberOfRounds=" + numberOfRounds + ", maxNumberOfPlayers=" + maxNumberOfPlayers + ", dictionary=" + dictionary + '}';
    }

}
