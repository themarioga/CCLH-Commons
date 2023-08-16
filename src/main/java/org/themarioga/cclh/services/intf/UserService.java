package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.User;

public interface UserService {

    User create(User user);

    User update(User user);

    void delete(User user);

    void deleteById(long id);

    User findOne(long id);

}
