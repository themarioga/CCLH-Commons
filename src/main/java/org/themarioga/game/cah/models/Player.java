package org.themarioga.game.cah.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Player")
public class Player extends org.themarioga.game.commons.models.Player implements Serializable {

    @Column(nullable = false)
    private Integer points = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    private List<PlayerHandCard> hand = new ArrayList<>(0);

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlayedCard playedCard;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VotedCard votedCard;

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
		if (!super.equals(o)) return false;

		Player player = (Player) o;
		return Objects.equals(getPoints(), player.getPoints());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(getPoints());
		return result;
	}

	@Override
    public String toString() {
        return "Player{" + super.toString() + ", points=" + points + '}';
    }
}
