package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomDoesntExistsException extends ApplicationException {

    public RoomDoesntExistsException() {
        super(ErrorEnum.ROOM_NOT_FOUND);
    }

}
