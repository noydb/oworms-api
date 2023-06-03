package com.oworms.auth.domain;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
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

    @Column(length = 75)
    private String email;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private final Status status = Status.ACTIVE;

    @ElementCollection
    private List<String> likedWordUUIDs;

    public User() {
    }

    public User(final String username, final String email) {
        this.username = username;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getLikedWordUUIDs() {
        return likedWordUUIDs;
    }

    public void setLikedWordUUIDs(List<String> likedWordUUIDs) {
        this.likedWordUUIDs = likedWordUUIDs;
    }
}