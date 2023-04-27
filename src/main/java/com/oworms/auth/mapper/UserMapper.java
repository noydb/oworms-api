package com.oworms.auth.mapper;

import com.oworms.auth.domain.Status;
import com.oworms.auth.domain.User;
import com.oworms.auth.dto.NewUserDTO;
import com.oworms.auth.dto.UserDTO;

public class UserMapper {

    private UserMapper() {
    }

    public static User map(NewUserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setStatus(Status.INACTIVE);

        return user;
    }

    public static UserDTO mapUser(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(user.getUuid());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setStatus(user.getStatus().getLabel());

        return userDTO;
    }

}
