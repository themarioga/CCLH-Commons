package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_player", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"})})
public class Player extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "join_order", nullable = false)
    private Integer joinOrder;
    @Column(name = "points")
    private Integer points;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayerHandCard> hand = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayedCard> playedCards = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<VotedCard> votedCards = new ArrayList<>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<VotedCard> getVotedCards() {
        return votedCards;
    }

    public void setVotedCards(List<VotedCard> votedCards) {
        this.votedCards = votedCards;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }
        return Objects.equals(id, ((Player) object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", joinOrder=" + joinOrder + ", points=" + points + '}';
    }
}
