package com.mikaauer.wtmauthorizationserver.UserDatabase;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isadmin;

    public Users() {

    }

    public Users(UUID id, String name, String password, Boolean isadmin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isadmin = isadmin;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }
}
