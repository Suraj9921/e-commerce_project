package com.shopme.common.entity;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdBasedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}