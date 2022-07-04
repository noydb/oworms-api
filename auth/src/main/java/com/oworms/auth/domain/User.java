package com.oworms.auth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "ow_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 36, updatable = false)
    private final String uuid = UUID.randomUUID().toString();

    @Column(length = 25)
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    @Size(max = 50)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}