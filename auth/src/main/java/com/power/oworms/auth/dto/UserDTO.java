package com.power.oworms.auth.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class UserDTO {

    private Long id;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Status cannot be null")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

