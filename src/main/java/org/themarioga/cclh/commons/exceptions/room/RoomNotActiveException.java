package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomNotActiveException extends ApplicationException {

    public RoomNotActiveException() {
        super(ErrorEnum.ROOM_NOT_ACTIVE);
    }

}
