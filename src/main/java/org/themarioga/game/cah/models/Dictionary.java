package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.commons.models.Base;
import org.themarioga.game.commons.models.Lang;
import org.themarioga.game.commons.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Dictionary extends Base {

    @Column(length = 256, nullable = false)
    private String name;
    @Column(nullable = false)
    private Boolean shared;
    @Column(nullable = false)
    private Boolean published;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User creator;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Lang lang;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dictionary", orphanRemoval = true)
    private List<DictionaryCollaborator> collaborators = new ArrayList<>(0);

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

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public List<DictionaryCollaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<DictionaryCollaborator> dictionaryCollaborators) {
        this.collaborators = dictionaryCollaborators;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dictionary dictionary = (Dictionary) object;
        return Objects.equals(getId(), dictionary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Deck{id=" + getId() + ", name='" + name + '\'' + ", shared=" + shared + ", published=" + published + '}';
    }

}
