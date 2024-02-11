package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@jakarta.persistence.Table(name = "t_dictionary_collaborators")
public class DictionaryCollaborator implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id")
    private Dictionary dictionary;

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "can_edit", nullable = false)
    private Boolean canEdit;
    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "DeckCollaborator{canEdit=" + canEdit + ", accepted=" + accepted + '}';
    }

}
