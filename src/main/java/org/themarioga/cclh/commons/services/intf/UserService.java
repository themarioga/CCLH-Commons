package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Lang;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface UserService {

    User createOrReactivate(long id, String name, Lang language);

    User rename(User user, String newName);

    User setActive(User user, boolean active);

    User setLanguage(User user, Lang language);

    User getById(long id);

    User getByUsername(String username);

    List<User> getAllUsers();

}
