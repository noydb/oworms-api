package com.oworms.auth.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Validated
public class UserDTO {

    private String uuid;

    @NotEmpty(message = "Username is required")
    @Size(max = 25)
    private String username;

    @NotEmpty(message = "Email is required")
    @Size(max = 125)
    private String email;

    @NotEmpty(message = "Status cannot be null")
    @Size(max = 50)
    private String status;

    private String wordsAdded;

    private String wordsAddedPercentage;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWordsAdded() {
        return wordsAdded;
    }

    public void setWordsAdded(String wordsAdded) {
        this.wordsAdded = wordsAdded;
    }

    public String getWordsAddedPercentage() {
        return wordsAddedPercentage;
    }

    public void setWordsAddedPercentage(String wordsAddedPercentage) {
        this.wordsAddedPercentage = wordsAddedPercentage;
    }
}

