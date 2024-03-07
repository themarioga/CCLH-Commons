package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface UserService {

    User createOrReactivate(long id, String name);

    User rename(User user, String newName);

    User setActive(User user, boolean active);

    User getById(long id);

    User getByUsername(String username);

    List<User> getAllUsers();

}
