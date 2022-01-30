package com.power.oworms.auth.dto;

import com.power.oworms.auth.annotation.PasswordMatches;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@PasswordMatches
@Validated
public class UpdatePasswordDTO {

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Password confirmation is required")
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
