package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.User;

public interface UserService {

    User createOrReactivate(long id, String name);

    User getById(long id);
}
