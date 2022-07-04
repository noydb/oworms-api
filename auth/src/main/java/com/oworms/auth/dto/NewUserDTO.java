package com.oworms.auth.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Validated
public class NewUserDTO {

    @NotEmpty(message = "Username is required")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NewUserDTO that = (NewUserDTO) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }
}
