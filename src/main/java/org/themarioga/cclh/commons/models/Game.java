package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;
import org.themarioga.cclh.commons.enums.GameStatusEnum;
import org.themarioga.cclh.commons.enums.GameTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_game", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id"})})
public class Game extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "status", nullable = false)
    private GameStatusEnum status;
    @Column(name = "type", nullable = false)
    private GameTypeEnum type;
    @Column(name = "n_cards_to_win", nullable = false)
    private Integer numberOfCardsToWin;
    @Column(name = "n_players", nullable = false)
    private Integer maxNumberOfPlayers;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Room room;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private User creator;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id", nullable = false)
    private Deck deck;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Table table;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Player> players = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_game_deletionvotes", joinColumns = @JoinColumn(name = "game_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "player_id", nullable = false))
    private List<Player> deletionVotes = new ArrayList<>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameTypeEnum getType() {
        return type;
    }

    public void setType(GameTypeEnum type) {
        this.type = type;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public Integer getNumberOfCardsToWin() {
        return numberOfCardsToWin;
    }

    public void setNumberOfCardsToWin(Integer numberOfCardsToWin) {
        this.numberOfCardsToWin = numberOfCardsToWin;
    }

    public GameStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GameStatusEnum currentStatus) {
        this.status = currentStatus;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getDeletionVotes() {
        return deletionVotes;
    }

    public void setDeletionVotes(List<Player> deletionVotes) {
        this.deletionVotes = deletionVotes;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Game card = (Game) object;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", type=" + type + ", maxNumberOfPlayers=" + maxNumberOfPlayers + ", numberOfCardsToWin=" + numberOfCardsToWin + ", status=" + status + "}";
    }

}
