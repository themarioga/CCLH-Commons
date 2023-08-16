package org.themarioga.cclh.models;

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "t_user")
public class User extends Base {

    @Id
    private Long id;
    @Column(name = "name", length = 256, nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private Boolean active;

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

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", active=" + active + '}';
    }

}
