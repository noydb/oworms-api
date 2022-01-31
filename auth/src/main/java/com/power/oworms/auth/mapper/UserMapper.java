package com.power.oworms.auth.mapper;

import com.power.oworms.auth.domain.Status;
import com.power.oworms.auth.domain.User;
import com.power.oworms.auth.dto.NewUserDTO;
import com.power.oworms.auth.dto.UserDTO;

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
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setStatus(user.getStatus().getLabel());

        return userDTO;
    }

}
