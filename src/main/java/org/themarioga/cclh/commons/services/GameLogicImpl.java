package org.themarioga.cclh.commons.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.exceptions.user.UserAlreadyExistsException;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.UserService;
import org.themarioga.cclh.commons.util.Assert;
import org.themarioga.cclh.commons.util.IStandardCallback;

public class GameLogicImpl {

    @Autowired
    UserService userService;

    public void userSignIn(User user, IStandardCallback callback) {
        Assert.assertNotNull(user, "El usuario no puede ser nulo");
        Assert.assertNotNull(callback, "El callback no puede ser nulo");
        Assert.assertNotEmpty(user.getName(), "El usuario no puede ser nulo");

        try {
            userService.create(user);

            callback.onSuccess();
        } catch (UserAlreadyExistsException e) {
            callback.onFailure(e);
        }
    }

}
