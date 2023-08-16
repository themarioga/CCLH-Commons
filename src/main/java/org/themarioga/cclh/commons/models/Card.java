package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;
import org.themarioga.cclh.commons.enums.CardTypeEnum;

@Entity
@jakarta.persistence.Table(name = "t_card")
public class Card extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type", length = 1, nullable = false)
    private CardTypeEnum type;
    @Column(name = "text", length = 256, nullable = false)
    private String text;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id")
    private Dictionary dictionary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    public String toString() {
        return "Card{" + "id=" + id + ", type=" + type + ", text='" + text + '\'' + '}';
    }

}
