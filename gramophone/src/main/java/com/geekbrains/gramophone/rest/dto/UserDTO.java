package com.geekbrains.gramophone.rest.dto;

import com.geekbrains.gramophone.entities.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UserDTO() {
    }
}
