package org.themarioga.cclh.models;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Date;

public class Base implements Serializable {

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
