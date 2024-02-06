package com.mikaauer.wtmauthorizationserver.User.DataTransfer;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private boolean isAdmin;

    public UserDTO(UUID id, String name, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
