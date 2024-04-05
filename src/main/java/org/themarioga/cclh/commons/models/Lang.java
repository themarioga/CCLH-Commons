package org.themarioga.cclh.commons.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_lang")
public class Lang implements Serializable {

    @Id
    private String id;
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lang lang = (Lang) o;
        return Objects.equals(id, lang.id) && Objects.equals(name, lang.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Lang{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
