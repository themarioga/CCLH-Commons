package org.themarioga.cclh.commons.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.themarioga.cclh.commons.logic.intf.GameLogic;
import org.themarioga.cclh.commons.services.intf.UserService;

@Component
public class GameLogicImpl implements GameLogic {

    @Autowired
    UserService userService;

}