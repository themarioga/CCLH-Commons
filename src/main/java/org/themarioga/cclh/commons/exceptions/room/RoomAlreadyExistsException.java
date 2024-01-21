package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomAlreadyExistsException extends ApplicationException {

    public RoomAlreadyExistsException() {
        super(ErrorEnum.ROOM_ALREADY_EXISTS);
    }

}
