package com.power.oworms.auth.validator;

import com.power.oworms.auth.annotation.PasswordMatches;
import com.power.oworms.auth.dto.NewUserDTO;
import com.power.oworms.auth.dto.UpdatePasswordDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof UpdatePasswordDTO) {
            UpdatePasswordDTO updatePassword = (UpdatePasswordDTO) obj;

            return updatePassword.getPassword().equals(updatePassword.getConfirmPassword());
        } else if (obj instanceof NewUserDTO) {
            NewUserDTO newUserDTO = (NewUserDTO) obj;

            return newUserDTO.getPassword().equals(newUserDTO.getConfirmPassword());
        }

        return false;
    }
}
