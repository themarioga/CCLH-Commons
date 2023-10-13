package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomNotActiveException extends ApplicationException {

    private final long id;

    public RoomNotActiveException(long id) {
        super(ErrorEnum.ROOM_NOT_ACTIVE);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
