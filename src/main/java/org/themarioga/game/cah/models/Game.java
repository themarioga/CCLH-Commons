package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.cah.enums.PunctuationModeEnum;
import org.themarioga.game.cah.enums.VotationModeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
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

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Player creatorPlayer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game", orphanRemoval = true)
    private List<Player> players = new ArrayList<>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_black_cards_deck", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> blackCardsDeck = new ArrayList<>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_white_cards_deck", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> whiteCardsDeck = new ArrayList<>(0);

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

    public Player getCreatorPlayer() {
        return creatorPlayer;
    }

    public void setCreatorPlayer(Player creatorPlayer) {
        this.creatorPlayer = creatorPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Card> getBlackCardsDeck() {
        return blackCardsDeck;
    }

    public void setBlackCardsDeck(List<Card> deckBlackCards) {
        this.blackCardsDeck = deckBlackCards;
    }

    public List<Card> getWhiteCardsDeck() {
        return whiteCardsDeck;
    }

    public void setWhiteCardsDeck(List<Card> deckWhiteCards) {
        this.whiteCardsDeck = deckWhiteCards;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Game game = (Game) o;
        return getVotationMode() == game.getVotationMode() && getPunctuationMode() == game.getPunctuationMode() && Objects.equals(getNumberOfPointsToWin(), game.getNumberOfPointsToWin()) && Objects.equals(getNumberOfRounds(), game.getNumberOfRounds()) && Objects.equals(getMaxNumberOfPlayers(), game.getMaxNumberOfPlayers()) && Objects.equals(getDictionary(), game.getDictionary());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getVotationMode());
        result = 31 * result + Objects.hashCode(getPunctuationMode());
        result = 31 * result + Objects.hashCode(getNumberOfPointsToWin());
        result = 31 * result + Objects.hashCode(getNumberOfRounds());
        result = 31 * result + Objects.hashCode(getMaxNumberOfPlayers());
        result = 31 * result + Objects.hashCode(getDictionary());
        return result;
    }

    @Override
    public String toString() {
        return "Game{" + super.toString() + "currentRound=" + currentRound + ", votationMode=" + votationMode + ", punctuationMode=" + punctuationMode + ", numberOfCardsToWin=" + numberOfPointsToWin + ", numberOfRounds=" + numberOfRounds + ", maxNumberOfPlayers=" + maxNumberOfPlayers + ", dictionary=" + dictionary + '}';
    }

}
