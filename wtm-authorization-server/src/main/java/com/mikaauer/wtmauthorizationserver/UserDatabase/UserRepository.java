package com.mikaauer.wtmauthorizationserver.UserDatabase;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<Users, UUID> {
    List<Users> findByName(String name);
}
