package com.mikaauer.wtmauthorizationserver.User.DataTransfer;

import com.mikaauer.wtmauthorizationserver.User.Users;

import java.util.List;

public class UsersResponse {

    private List<UserDTO> items;

    public UsersResponse(List<UserDTO> items) {
        this.items = items;
    }

    public List<UserDTO> getItems() {
        return items;
    }

    public void setItems(List<UserDTO> items) {
        this.items = items;
    }
}
