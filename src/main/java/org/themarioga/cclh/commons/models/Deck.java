package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_dictionary")
public class Deck extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 256, nullable = false)
    private String name;
    @Column(name = "shared", nullable = false)
    private Boolean shared;
    @Column(name = "published", nullable = false)
    private Boolean published;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_card", joinColumns = @JoinColumn(name = "dictionary_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "id", nullable = false))
    private List<Card> cards = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id")
    private List<DeckCollaborator> collaborators = new ArrayList<>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<DeckCollaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<DeckCollaborator> deckCollaborators) {
        this.collaborators = deckCollaborators;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Deck card = (Deck) object;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Deck{" + "id=" + id + ", name='" + name + '\'' + ", shared=" + shared + ", published=" + published + '}';
    }

}
