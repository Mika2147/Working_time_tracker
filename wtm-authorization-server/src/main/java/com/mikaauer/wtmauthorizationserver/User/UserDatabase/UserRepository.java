package com.mikaauer.wtmauthorizationserver.User.UserDatabase;

import com.mikaauer.wtmauthorizationserver.User.Users;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<Users, UUID> {
    List<Users> findByName(String name);
}
