package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomDoesntExistsException extends ApplicationException {

    private final long id;

    public RoomDoesntExistsException(long id) {
        super(ErrorEnum.ROOM_NOT_FOUND);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
