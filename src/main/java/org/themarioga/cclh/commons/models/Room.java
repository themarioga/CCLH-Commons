package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "t_room")
public class Room extends Base {

    @Id
    private Long id;
    @Column(name = "name", length = 256, nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name='" + name + '\'' + ", active=" + active + '}';
    }

}
