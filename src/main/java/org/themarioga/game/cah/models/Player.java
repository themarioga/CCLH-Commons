package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.commons.models.Base;
import org.themarioga.game.commons.models.Game;
import org.themarioga.game.commons.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Player")
public class Player extends Base implements Serializable {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Game game;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Integer joinOrder;

    @Column(nullable = false)
    private Integer points = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayerHandCard> hand = new ArrayList<>(0);

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlayedCard playedCard;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VotedCard votedCard;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getJoinOrder() {
        return joinOrder;
    }

    public void setJoinOrder(Integer joinOrder) {
        this.joinOrder = joinOrder;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<PlayerHandCard> getHand() {
        return hand;
    }

    public void setHand(List<PlayerHandCard> hand) {
        this.hand = hand;
    }

    public PlayedCard getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(PlayedCard playedCard) {
        this.playedCard = playedCard;
    }

    public VotedCard getVotedCard() {
        return votedCard;
    }

    public void setVotedCard(VotedCard votedCard) {
        this.votedCard = votedCard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return Objects.equals(getGame(), player.getGame()) && Objects.equals(getUser(), player.getUser()) && Objects.equals(getJoinOrder(), player.getJoinOrder()) && Objects.equals(getPoints(), player.getPoints());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getPoints());
        return result;
    }

    @Override
    public String toString() {
        return "Player{" + "game=" + game + ", user=" + user + ", joinOrder=" + joinOrder + ", points=" + points + '}';
    }

}
