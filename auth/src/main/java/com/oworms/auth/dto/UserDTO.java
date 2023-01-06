package com.oworms.auth.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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

    private List<String> likedWordUUIDs;

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

    public List<String> getLikedWordUUIDs() {
        return likedWordUUIDs;
    }

    public void setLikedWordUUIDs(List<String> likedWordUUIDs) {
        this.likedWordUUIDs = likedWordUUIDs;
    }
}
