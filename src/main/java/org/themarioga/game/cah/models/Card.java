package org.themarioga.game.cah.models;

import jakarta.persistence.*;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.commons.models.Base;

import java.util.Objects;

@Entity
public class Card extends Base {

    @Column(length = 1, nullable = false)
    private CardTypeEnum type;
    @Column(length = 256, nullable = false)
    private String text;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn()
    private Dictionary dictionary;

    public CardTypeEnum getType() {
        return type;
    }

    public void setType(CardTypeEnum type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Card card = (Card) object;
        return Objects.equals(getId(), card.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Card{id=" + getId() + ", type=" + type + ", text='" + text + '\'' + '}';
    }

}
